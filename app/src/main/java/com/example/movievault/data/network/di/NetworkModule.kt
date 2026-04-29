package com.example.movievault.data.network.di

import com.example.movievault.BuildConfig
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
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json { ignoreUnknownKeys = true }
    @Provides
    @Singleton
    fun providesOkhttpCallFactory(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    }
            )
            .addInterceptor(
                AuthInterceptor(BuildConfig.TMD_TOKEN)
            )
            .build()

}

@Module
@InstallIn(SingletonComponent::class)
abstract class MovaDataSource {

    @Binds
    abstract fun bindsMovaNetworkDataSource(impl: MovaNetworkRetrofit): MovaNetworkDataSource
}