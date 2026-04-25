package com.example.movievault.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movievault.data.model.Actor

@Entity("actor")
data class ActorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val profilePath: String,
    val character: String,
    val movieId: Int
)

fun ActorEntity.asExternalModel() = Actor(
    name = name,
    profilePath = profilePath,
    character = character
)

fun List<ActorEntity>.toDomain() = this.map(ActorEntity::asExternalModel)
