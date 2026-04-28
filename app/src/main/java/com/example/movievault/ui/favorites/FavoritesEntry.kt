package com.example.movievault.ui.favorites

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.movievault.navigation.MovaDestinations

fun EntryProviderScope<NavKey>.favoritesEntry(onFavoriteClick : (Int) -> Unit) {
    entry<MovaDestinations.FavoritesNavKey> {
        FavoritesScreen(
            onFavoriteClick = onFavoriteClick
        )
    }
}