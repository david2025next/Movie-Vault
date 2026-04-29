package com.example.movievault.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movievault.data.database.dao.ActorDao
import com.example.movievault.data.database.dao.MovieDao
import com.example.movievault.data.database.dao.RemoteKeysDao
import com.example.movievault.data.database.model.ActorEntity
import com.example.movievault.data.database.model.MovieEntity
import com.example.movievault.data.database.model.RemoteKeysEntity

@Database(
    entities = [MovieEntity::class, RemoteKeysEntity::class, ActorEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieVaultDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    abstract fun actorDao(): ActorDao
}