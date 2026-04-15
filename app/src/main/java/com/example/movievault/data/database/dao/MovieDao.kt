package com.example.movievault.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Upsert
import com.example.movievault.data.database.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = IGNORE)
    suspend fun insertOrIgnoreMovies(movieEntities : List<MovieEntity>)

    @Query(
        value = """
        SELECT * FROM movie
    """)
    fun getMoviesStream() : Flow<List<MovieEntity>>


    @Upsert
    suspend fun upsertMovies(entities : List<MovieEntity>)

    @Query(
        value  = """
            DELETE FROM movie
            WHERE id in (:ids)
        """
    )
    suspend fun deleteMovies(ids : List<Int>)
}