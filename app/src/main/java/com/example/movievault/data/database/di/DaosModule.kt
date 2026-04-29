package com.example.movievault.data.database.di

import com.example.movievault.data.database.MovieVaultDatabase
import com.example.movievault.data.database.dao.ActorDao
import com.example.movievault.data.database.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {


    @Provides
    fun providesActorDao(movieVaultDatabase: MovieVaultDatabase): ActorDao =
        movieVaultDatabase.actorDao()

    @Provides
    fun providesMovieDao(movieVaultDatabase: MovieVaultDatabase): MovieDao =
        movieVaultDatabase.movieDao()
}
