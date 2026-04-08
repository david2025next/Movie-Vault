package com.example.movievault.data.repository

import com.example.movievault.data.model.Movie
import com.example.movievault.domain.DataErrors
import com.example.movievault.domain.Result

interface MovieRepository {

    suspend fun getMovies(page : Int = 1) : Result<List<Movie>, DataErrors>
}