package com.example.movievault.data.repository

import androidx.paging.PagingData
import com.example.movievault.data.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(): Flow<PagingData<Movie>>

    fun getMovieFlow(id: Int): Flow<Movie>

    suspend fun toggleFavorite(movieId: Int, isFavorite: Boolean)

}