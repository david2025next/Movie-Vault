package com.example.movievault.data.database.di

import android.content.Context
import androidx.room.Room
import com.example.movievault.data.database.dao.MovieDao
import com.example.movievault.data.database.MovieVaultDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesMovieDatabase(@ApplicationContext context : Context) : MovieVaultDatabase = Room.databaseBuilder(
        context,
        MovieVaultDatabase::class.java,
        "movie_vault_db"
    ).build()

    @Provides
    @Singleton
    fun providesMovieDao(movieVaultDatabase: MovieVaultDatabase) : MovieDao = movieVaultDatabase.movieDao
}