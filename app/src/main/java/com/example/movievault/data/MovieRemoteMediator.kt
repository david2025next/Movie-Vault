package com.example.movievault.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.movievault.data.database.MovieVaultDatabase
import com.example.movievault.data.database.model.MovieEntity
import com.example.movievault.data.database.model.RemoteKeysEntity
import com.example.movievault.data.network.MovaNetworkDataSource
import com.example.movievault.data.network.model.toEntities

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movaNetworkDataSource: MovaNetworkDataSource,
    private val movieVaultDatabase: MovieVaultDatabase
) : RemoteMediator<Int, MovieEntity>() {

    companion object {
        const val TMDB_STARTING_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val refreshKey = getRemoteKeyClosestToCurrentPosition(state)
                refreshKey?.nextKey?.minus(1) ?: TMDB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val refreshKey = getRemoteKeyForFirstItem(state)
                val prevKey = refreshKey?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = refreshKey != null
                )
                prevKey
            }

            LoadType.APPEND -> {
                val refreshKey = getRemoteKeyForLastItem(state)
                val nextKey = refreshKey?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = refreshKey != null
                )
                nextKey
            }
        }

        try {
            val moviesNetwork = movaNetworkDataSource.getMovies(page = page)
            val endOfPaginationReached = moviesNetwork.isEmpty()
            movieVaultDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    movieVaultDatabase.remoteKeysDao.clearRemoteKeys()
                    movieVaultDatabase.movieDao.clearMovies()
                }
                val prevKey = if (page == TMDB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = moviesNetwork.map {
                    RemoteKeysEntity(
                        movieId = it.id, prevKey = prevKey, nextKey = nextKey
                    )
                }
                movieVaultDatabase.remoteKeysDao.insertAll(keys)
                movieVaultDatabase.movieDao.insertAll(moviesNetwork.toEntities())
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (ex: Exception) {
            return MediatorResult.Error(ex)
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