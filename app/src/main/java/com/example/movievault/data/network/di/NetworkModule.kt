package com.example.movievault.data.network.di

import com.example.movievault.data.network.MovaNetworkDataSource
import com.example.movievault.data.network.interceptors.AuthInterceptor
import com.example.movievault.data.network.retrofit.MovaNetworkRetrofit
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson() : Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun providesOkhttpCallFactory() : OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
            )
            .addInterceptor(
                AuthInterceptor("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlYWM0ZDNjMDUzZDJiOGNiZjFmY2Q1OTI0MmZjNTg1YSIsIm5iZiI6MTc3NTAzMTgwOS4xNjgsInN1YiI6IjY5Y2NkNjAxODNhYWI4NzZjYmNlMDRmZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.X4yWmwN3zo-THQkKU3Zq8q-GHRd7DMTpzu0O_8GB6NI")
            )
            .build()

}

@Module
@InstallIn(SingletonComponent::class)
abstract class MovaDataSource{

    @Binds
    abstract fun bindsMovaNetworkDataSource(impl : MovaNetworkRetrofit) : MovaNetworkDataSource
}