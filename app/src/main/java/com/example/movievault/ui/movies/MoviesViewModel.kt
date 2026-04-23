package com.example.movievault.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.movievault.data.repository.MovieRepository
import com.example.movievault.data.utils.getDataError
import com.example.movievault.domain.DataErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _error = MutableStateFlow<DataErrors?>(null)
    val error = _error.asStateFlow()
    val uiState = movieRepository.getMovies().cachedIn(viewModelScope)

    fun errorHandler(error: Throwable) {
        val ex = error as Exception
        val dataError = ex.getDataError()
        _error.update { dataError }
    }

    fun resetError() {
        _error.update { null }
    }

}

