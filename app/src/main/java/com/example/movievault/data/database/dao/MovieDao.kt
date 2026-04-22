package com.example.movievault.data.database.dao

import androidx.paging.Pager
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Upsert
import com.example.movievault.data.database.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(movies : List<MovieEntity>)

    @Insert(onConflict = IGNORE)
    suspend fun insertOrIgnoreMovies(movieEntities : List<MovieEntity>)

    @Query(
        value = """
        SELECT * FROM movie ORDER BY popularity DESC
    """)
    fun getPagingMovies() : PagingSource<Int, MovieEntity>


    @Query(
        value = """
            SELECT MAX(updateAt) FROM movie
        """
    )
    suspend fun lastUpdated() : Long
    @Upsert
    suspend fun upsertMovies(entities : List<MovieEntity>)

    @Query(
        value  = """
            DELETE FROM movie
            WHERE id in (:ids)
        """
    )
    suspend fun deleteMovies(ids : List<Int>)

    @Query(
        value = """
            DELETE FROM movie
        """
    )
    suspend fun clearMovies()
}