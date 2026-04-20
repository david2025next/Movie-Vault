package com.example.movievault.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movievault.data.database.dao.MovieDao
import com.example.movievault.data.database.dao.RemoteKeysDao
import com.example.movievault.data.database.model.MovieEntity
import com.example.movievault.data.database.model.RemoteKeysEntity

@Database(
    entities = [MovieEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieVaultDatabase : RoomDatabase() {

    abstract val movieDao : MovieDao

    abstract val remoteKeysDao : RemoteKeysDao
}