package com.example.movievault.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.movievault.data.database.MovieVaultDatabase
import com.example.movievault.data.database.model.MovieEntity
import com.example.movievault.data.database.model.RemoteKeysEntity
import com.example.movievault.data.di.Dispatcher
import com.example.movievault.data.di.MovaDispatchers
import com.example.movievault.data.network.MovaNetworkDataSource
import com.example.movievault.data.network.model.toEntities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movaNetworkDataSource: MovaNetworkDataSource,
    private val movieVaultDatabase: MovieVaultDatabase,
    private val dispatcher: CoroutineDispatcher
) : RemoteMediator<Int, MovieEntity>() {

    val movieDao = movieVaultDatabase.movieDao
    val remoteKeysDao = movieVaultDatabase.remoteKeysDao

    companion object {
        const val TMDB_STARTING_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.HOURS.toMillis(1)
        val lastUpdated = movieDao.lastUpdated()
        val shouldRefresh =
            lastUpdated == 0L || System.currentTimeMillis() - lastUpdated >= cacheTimeout
        return if (shouldRefresh) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else InitializeAction.SKIP_INITIAL_REFRESH
    }


    @OptIn(ExperimentalPagingApi::class)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                getRemoteKeyClosestToCurrentPosition(state)?.nextKey?.minus(1)
                    ?: TMDB_STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKey?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                prevKey
            }

            LoadType.APPEND -> {
                Log.d("TAG", "load: append")
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                nextKey
            }
        }

        return try {
            Log.d("tag", "load: $page")
            val moviesNetwork = movaNetworkDataSource.getMovies(page = page)
            val endOfPaginationReached = moviesNetwork.isEmpty()

            val prevKey = if (page == TMDB_STARTING_PAGE_INDEX) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val remotesKey = moviesNetwork.map {
                RemoteKeysEntity(
                    movieId = it.id,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
            movieVaultDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    movieDao.clearMovies()
                }
                remoteKeysDao.insertAll(remotesKey)
                movieDao.insertAll(moviesNetwork.toEntities())
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }

    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state
            .pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data
            ?.lastOrNull()
            ?.let { movieEntity -> movieVaultDatabase.remoteKeysDao.remoteKeyMovieId(movieEntity.id) }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {

        return state
            .pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data
            ?.firstOrNull()
            ?.let { movieEntity -> movieVaultDatabase.remoteKeysDao.remoteKeyMovieId(movieEntity.id) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieEntity>
    ): RemoteKeysEntity? {
        return state
            .anchorPosition
            ?.let { position ->
                state.closestItemToPosition(position)?.id?.let { movieId ->
                    movieVaultDatabase.remoteKeysDao.remoteKeyMovieId(movieId)
                }
            }
    }

}