package com.example.movievault.data.repository

import com.example.movievault.data.model.Movie
import com.example.movievault.data.network.MovaNetworkDataSource
import com.example.movievault.data.network.model.NetworkMovie
import com.example.movievault.data.network.model.asExternalModel
import com.example.movievault.data.utils.getDataError
import com.example.movievault.domain.DataErrors
import com.example.movievault.domain.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val movaNetworkDataSource: MovaNetworkDataSource
) : MovieRepository {

    override suspend fun getMovies(page: Int): Result<List<Movie>, DataErrors> {

        return try {
            val movies = movaNetworkDataSource.getMovies(page).map(NetworkMovie::asExternalModel)
            Result.Success<List<Movie>, DataErrors>(movies)
        } catch (e: Exception) {
            val errors = e.getDataError()
            Result.Error<List<Movie>, DataErrors>(errors)
        }
    }
}