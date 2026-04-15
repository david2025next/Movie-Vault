package com.example.movievault.data.repository

import com.example.movievault.data.model.Movie
import com.example.movievault.domain.DataErrors
import com.example.movievault.domain.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(page: Int = 1): Flow<List<Movie>>

    suspend fun refreshMovies() : Result<Unit, DataErrors>
}