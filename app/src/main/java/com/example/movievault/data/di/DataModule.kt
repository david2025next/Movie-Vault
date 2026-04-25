package com.example.movievault.data.di

import com.example.movievault.data.repository.ActorRepository
import com.example.movievault.data.repository.ActorRepositoryImpl
import com.example.movievault.data.repository.MovieRepository
import com.example.movievault.data.repository.OffLineFirstMoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsMovieRepository(impl : OffLineFirstMoviesRepository) : MovieRepository

    @Binds
    abstract fun bindsActorRepository(impl : ActorRepositoryImpl): ActorRepository
}