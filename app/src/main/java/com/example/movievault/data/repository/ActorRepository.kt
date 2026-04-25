package com.example.movievault.data.repository

import com.example.movievault.data.model.Actor
import com.example.movievault.domain.DataErrors
import com.example.movievault.domain.Result
import kotlinx.coroutines.flow.Flow

interface ActorRepository {

    fun getActorsForMovieWithId(movieId: Int): Flow<List<Actor>>

    suspend fun syncActors(movieId: Int): Result<Unit, DataErrors>

}