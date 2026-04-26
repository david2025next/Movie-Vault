package com.example.movievault.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Actor
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.ActorRepository
import com.example.movievault.data.repository.MovieRepository
import com.example.movievault.domain.DataErrors
import com.example.movievault.domain.Result
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = MovieDetailViewModel.Factory::class)
class MovieDetailViewModel @AssistedInject constructor(
    private val actorRepository: ActorRepository,
    private val movieRepository: MovieRepository,
    @Assisted val movieId: Int
) : ViewModel() {

    private val _errorUiState = MutableStateFlow<DataErrors?>(null)
    val errorUiState = _errorUiState.asStateFlow()

    val uiState = combine(
        actorRepository.getActorsForMovieWithId(movieId),
        movieRepository.getMovieFlow(movieId)
    ) { actors, movie ->
        MovieDetailUiState.Success(MovieActors(movie, actors))
    }
        .onStart {
            syncActors()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            MovieDetailUiState.Loading
        )

    @AssistedFactory
    interface Factory {
        fun create(movieId: Int): MovieDetailViewModel
    }


    fun onResetError() {
        _errorUiState.update { null }
    }

    fun syncActors() {
        viewModelScope.launch {
            when (val result = actorRepository.syncActors(movieId)) {
                is Result.Error -> {
                    _errorUiState.update { result.error }
                }

                is Result.Success -> {}
            }
        }
    }

    fun onToggleFavorites(isFavorite: Boolean) {
        viewModelScope.launch {
            movieRepository.toggleFavorite(movieId, isFavorite)
        }
    }
}


sealed interface MovieDetailUiState {
    object Loading : MovieDetailUiState
    data class Success(val data: MovieActors) : MovieDetailUiState
}

data class MovieActors(
    val movie: Movie,
    val actors: List<Actor>
)