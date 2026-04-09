package com.example.movievault.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.MovieRepository
import com.example.movievault.domain.DataErrors
import com.example.movievault.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Error(val error: DataErrors) : HomeUiState
    data class Success(val movies: List<MovieItemUiState>) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        initialize()
    }

    private fun initialize() {
        _uiState.update { HomeUiState.Loading }
        viewModelScope.launch {
            val newState = when (val result = movieRepository.getMovies()) {
                is Result.Error -> {
                    HomeUiState.Error(result.error)
                }

                is Result.Success -> {
                    val moviesUi = result.data.map(Movie::toUi)
                    HomeUiState.Success(moviesUi)
                }
            }

            _uiState.update { newState }
        }
    }

    fun retry(){
        initialize()
    }
}


fun Movie.toUi() = MovieItemUiState(
    id = id,
    title = title,
    posterPath = posterPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount
)

data class MovieItemUiState(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val posterPath: String,
    val voteAverage : Double,
    val voteCount : Int
)