package com.example.movievault.data.repository

import com.example.movievault.data.database.dao.MovieDao
import com.example.movievault.data.database.model.toEntities
import com.example.movievault.data.database.model.toMovies
import com.example.movievault.data.model.Movie
import com.example.movievault.data.network.MovaNetworkDataSource
import com.example.movievault.data.network.model.toMovies
import com.example.movievault.data.utils.getDataError
import com.example.movievault.domain.DataErrors
import com.example.movievault.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OffLineFirstMoviesRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val movaNetworkDataSource: MovaNetworkDataSource,
) : MovieRepository {


    override fun getMovies(page: Int): Flow<List<Movie>> {
        return movieDao.getMoviesStream().map { moviesStream -> moviesStream.toMovies() }
    }

    override suspend fun refreshMovies(): Result<Unit, DataErrors> {
        return try {
            val movies = movaNetworkDataSource.getMovies(1).toMovies()
            movieDao.upsertMovies(movies.toEntities())
            Result.Success(Unit)
        } catch (ex: Exception) {
            val error = ex.getDataError()
            Result.Error(error)
        }
    }
}