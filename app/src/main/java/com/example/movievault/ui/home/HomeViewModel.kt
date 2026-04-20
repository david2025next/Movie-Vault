package com.example.movievault.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.movievault.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val uiState = movieRepository.getMovies()

}