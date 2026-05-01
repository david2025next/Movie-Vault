package com.example.movievault.data.network.di

import android.content.Context
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.util.DebugLogger
import com.example.movievault.BuildConfig
import com.example.movievault.data.network.MovaNetworkDataSource
import com.example.movievault.data.network.interceptors.AuthInterceptor
import com.example.movievault.data.network.retrofit.MovaNetworkRetrofit
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesNetworkJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory =
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

    @Provides
    @Singleton
    fun imageLoader(
        okHttpCallFactoryFactory: dagger.Lazy<Call.Factory>,
        @ApplicationContext application: Context
    ): ImageLoader = ImageLoader
        .Builder(application)
        .components {
            add(
                OkHttpNetworkFetcherFactory(
                    callFactory = { okHttpCallFactoryFactory.get() }
                )
            )
        }
        .apply {
            if (BuildConfig.DEBUG) {
                logger(DebugLogger())
            }
        }
        .build()


}

@Module
@InstallIn(SingletonComponent::class)
abstract class MovaDataSource {

    @Binds
    abstract fun bindsMovaNetworkDataSource(impl: MovaNetworkRetrofit): MovaNetworkDataSource
}