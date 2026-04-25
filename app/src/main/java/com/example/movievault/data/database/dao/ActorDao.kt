package com.example.movievault.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.movievault.data.database.model.ActorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActorDao {

    @Query(
        value = "SELECT * FROM actor WHERE movieId =:movieId"
    )
    fun getActorsForMovieIdStream(movieId: Int): Flow<List<ActorEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(actors: List<ActorEntity>)

    @Query(
        value = """
            SELECT COUNT(*) FROM actor WHERE movieId =:movieId
        """
    )
    suspend fun countActors(movieId: Int): Long
}