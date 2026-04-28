package com.example.movievault.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface FavoritesUiState {

    val isLoading: Boolean
    data class HasContent(val favorites: List<Movie>, override val isLoading: Boolean) :
        FavoritesUiState
    data class NoContent(override val isLoading: Boolean) : FavoritesUiState
}


private data class FavoritesViewModelState(
    val items: List<Movie> = emptyList(),
    val isLoading: Boolean = false
) {

    fun toUiState(): FavoritesUiState =
        if (items.isEmpty()) FavoritesUiState.NoContent(isLoading = isLoading)
        else FavoritesUiState.HasContent(favorites = items, isLoading = isLoading)
}

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(FavoritesViewModelState(isLoading = true))

    val uiState: StateFlow<FavoritesUiState> = viewModelState
        .onStart { initialize() }
        .map { viewModelState -> viewModelState.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            viewModelState.value.toUiState()
        )

    private fun initialize() {
        viewModelScope.launch {
            viewModelState.update { it.copy(isLoading = true) }
            movieRepository.getFavoritesMovie().collect { movies ->
                viewModelState.update { it.copy(items = movies, isLoading = false) }
            }
        }
    }

    fun onToggleFavorite(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            movieRepository.toggleFavorite(movieId, isFavorite)
        }
    }
}