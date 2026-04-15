package com.example.movievault.data.network.model

import com.example.movievault.data.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"

@Serializable
data class NetworkMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount : Int
)


private fun NetworkMovie.asExternalModel() = Movie(
    id = id,
    title = title,
    releaseDate = releaseDate,
    posterPath = "$IMAGE_URL$posterPath",
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun List<NetworkMovie>.toMovies() = this.map(NetworkMovie::asExternalModel)
