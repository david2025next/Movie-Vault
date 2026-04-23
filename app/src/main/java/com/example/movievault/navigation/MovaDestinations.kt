package com.example.movievault.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface MovaDestinations : NavKey{

    @Serializable
    data class MovieDetail(val id : Int) : MovaDestinations
    @Serializable
    object MovieListNavKey : MovaDestinations

    @Serializable
    object FavoritesNavKey : MovaDestinations
}