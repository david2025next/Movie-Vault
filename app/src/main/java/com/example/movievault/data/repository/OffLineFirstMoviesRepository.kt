package com.example.movievault.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.movievault.data.MovieRemoteMediator
import com.example.movievault.data.database.MovieVaultDatabase
import com.example.movievault.data.database.model.asExternalModel
import com.example.movievault.data.di.Dispatcher
import com.example.movievault.data.di.MovaDispatchers
import com.example.movievault.data.model.Movie
import com.example.movievault.data.network.MovaNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OffLineFirstMoviesRepository @Inject constructor(
    private val movaNetworkDataSource: MovaNetworkDataSource,
    private val movieVaultDatabase: MovieVaultDatabase,
    @Dispatcher(MovaDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : MovieRepository {
    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            prefetchDistance = 5,
            enablePlaceholders = false
        ),
        remoteMediator = MovieRemoteMediator(
            movieVaultDatabase = movieVaultDatabase,
            movaNetworkDataSource = movaNetworkDataSource,
            dispatcher = dispatcher
        ),
        pagingSourceFactory = { movieVaultDatabase.movieDao.getPagingMovies() }
    ).flow
        .map { value -> value.map { movieEntity -> movieEntity.asExternalModel() } }


}