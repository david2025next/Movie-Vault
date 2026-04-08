package com.example.movievault.data.di

import com.example.movievault.data.repository.MovieRepository
import com.example.movievault.data.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsMovieRepository(impl : MovieRepositoryImpl) : MovieRepository
}