package com.example.movievault.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movievault.data.model.Movie


@Entity("movie")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val releaseDate: String,
    val posterPath: String,
    val voteAverage: Double,
    val voteCount: Int
)

private fun MovieEntity.asExternalModel() = Movie(
    id = id,
    title = title,
    releaseDate = releaseDate,
    posterPath = posterPath,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun List<MovieEntity>.toMovies() = this.map(MovieEntity::asExternalModel)

private fun Movie.toEntity() = MovieEntity(
    id = id,
    title = title,
    releaseDate = releaseDate,
    posterPath = posterPath,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun List<Movie>.toEntities() = this.map(Movie::toEntity)
