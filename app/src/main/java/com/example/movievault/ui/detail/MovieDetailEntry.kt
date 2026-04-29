package com.example.movievault.ui.detail

import androidx.compose.material3.SnackbarResult
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.movievault.navigation.MovaDestinations
import com.example.movievault.ui.LocalSnackbarHostState

fun EntryProviderScope<NavKey>.movieDetailEntry(onBack: () -> Unit) {
    entry<MovaDestinations.MovieDetail> { key ->

        val viewModel = hiltViewModel<MovieDetailViewModel, MovieDetailViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(key.id)
            }
        )
        val snackbar = LocalSnackbarHostState.current
        MovieDetailScreen(
            onBack = onBack,
            onShowSnackbar = { message, action ->
                snackbar.showSnackbar(
                    message,
                    actionLabel = action
                ) == SnackbarResult.ActionPerformed
            },
            viewModel = viewModel
        )
    }
}