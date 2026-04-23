package com.example.movievault.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface MovaDestinations : NavKey {
    @Serializable
    object HomeNavKey : MovaDestinations

    @Serializable
    object FavoritesNavKey : MovaDestinations
}

