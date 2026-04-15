package com.example.movievault.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movievault.data.database.dao.MovieDao
import com.example.movievault.data.database.model.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieVaultDatabase : RoomDatabase() {

    abstract val movieDao : MovieDao
}