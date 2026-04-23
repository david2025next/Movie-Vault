package com.example.movievault.ui.movies

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.movievault.navigation.MovaDestinations
import com.example.movievault.ui.LocalSnackbarHostState

fun EntryProviderScope<NavKey>.movieListEntry(onMovieItemClick: (Int) -> Unit) {
    entry<MovaDestinations.MovieListNavKey> {
        val snackbarHostState = LocalSnackbarHostState.current
        MoviesListScreen(
            onMovieItemClicked = onMovieItemClick,
            onShowSnackbar = { message, action ->
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = action,
                    duration = SnackbarDuration.Short
                ) == SnackbarResult.ActionPerformed
            }
        )
    }
}