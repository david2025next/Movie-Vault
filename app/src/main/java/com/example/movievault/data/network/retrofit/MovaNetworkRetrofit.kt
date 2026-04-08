package com.example.movievault.data.network.retrofit

import com.example.movievault.data.network.MovaNetworkDataSource
import com.example.movievault.data.network.model.NetworkMovie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton


private interface MovaRetrofitApi{

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("page") page : Int,
        @Query("language") language : String
    ) : NetworkResponse<List<NetworkMovie>>
}

@Serializable
data class NetworkResponse<T>(

    @SerialName("results")
    val data : T,
    val page: Int,
)


private val BACKEND_URL  =  "https://api.themoviedb.org/3/"

@Singleton
class MovaNetworkRetrofit @Inject constructor(
    networkJson : Json,
    private val okHttpCallFactory : dagger.Lazy<OkHttpClient>
) : MovaNetworkDataSource {

    private val retrofit  = Retrofit.Builder()
        .baseUrl(BACKEND_URL)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType())
        )
        .callFactory {
            okHttpCallFactory.get().newCall(it)
        }
        .build()
        .create(MovaRetrofitApi::class.java)

    override suspend fun getMovies(
        page: Int,
        language: String
    ): List<NetworkMovie> = retrofit.getMovies(page, language).data
}