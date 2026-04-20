package com.example.movievault.data.repository

import androidx.paging.PagingData
import com.example.movievault.data.model.Movie
import com.example.movievault.domain.DataErrors
import com.example.movievault.domain.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(): Flow<PagingData<Movie>>

}