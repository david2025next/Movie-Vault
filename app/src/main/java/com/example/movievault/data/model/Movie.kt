package com.example.movievault.data.model

data class Movie(
    val id : Int,
    val title : String,
    val releaseDate : String,
    val posterPath : String?,
    val voteAverage : Double,
    val voteCount : Int,
    val isFavorite : Boolean,
    val overview : String,
    val popularity: Double
)