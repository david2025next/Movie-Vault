package com.example.movievault.data.network.model

import com.example.movievault.data.database.model.ActorEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
private const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
@Serializable
data class NetworkActor(
    val name: String,
    @SerialName("profile_path")
    val profilePath: String? = null,
    val character: String
)


fun NetworkActor.asEntity(movieId: Int) = ActorEntity(
    name = name,
    character = character,
    profilePath = if(profilePath!=null) "$IMAGE_URL/$profilePath" else null,
    movieId = movieId
)


