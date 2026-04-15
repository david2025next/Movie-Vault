package com.example.movievault.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.MovieRepository
import com.example.movievault.domain.DataErrors
import com.example.movievault.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieItemUiState(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val posterPath: String,
    val voteAverage: Double,
    val voteCount: Int
)

fun Movie.toUi() = MovieItemUiState(
    id = id,
    title = title,
    releaseDate = releaseDate,
    posterPath = posterPath,
    voteAverage = voteAverage,
    voteCount = voteCount
)

sealed interface HomeUiState {

    val isLoading: Boolean
    val error: DataErrors?

    data class NoMovies(override val isLoading: Boolean, override val error: DataErrors?) :
        HomeUiState

    data class HasMovies(
        val items: List<MovieItemUiState>, override val isLoading: Boolean,
        override val error: DataErrors?
    ) : HomeUiState
}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val error: DataErrors? = null,
    val movies: List<MovieItemUiState> = emptyList()
) {
    fun toUiState(): HomeUiState = if (movies.isEmpty()) {
        HomeUiState.NoMovies(isLoading, error)
    } else {
        HomeUiState.HasMovies(
            items = movies,
            isLoading = isLoading,
            error = error
        )
    }
}
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = true))

    val uiState = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            viewModelState.value.toUiState()
        )
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        viewModelScope.launch {
            movieRepository.getMovies(1).collect { movies ->
                viewModelState.update {
                    it.copy(
                        isLoading = false,
                        movies = movies.map { movie -> movie.toUi() })
                }
            }
        }
        onRefresh()
    }

    fun onRefresh() {
        viewModelScope.launch {
            viewModelState.update { it.copy(error = null) }
            _isRefreshing.update { true }
            when (val result = movieRepository.refreshMovies()) {
                is Result.Error -> {
                    viewModelState.update { it.copy(error = result.error) }
                }
                is Result.Success -> {}
            }
            _isRefreshing.update { false }
        }
    }
}