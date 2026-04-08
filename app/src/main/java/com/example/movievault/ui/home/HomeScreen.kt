package com.example.movievault.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.movievault.ui.utils.asUiText

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()){

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState
    )
}

@Composable
private fun HomeScreen(uiState: HomeUiState, modifier: Modifier = Modifier) {

    Scaffold(modifier) { paddingValues ->

        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (uiState) {
                is HomeUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(uiState.error.asUiText().asString())
                    }
                }
                HomeUiState.Loading -> {
                    LoadingScreen()
                }

                is HomeUiState.Success -> {
                    MoviesList(uiState.movies)
                }
            }
        }
    }
}



@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun MoviesList(
    movies: List<MovieItemUiState>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            MovieCard(movieItem = movie)
        }
    }
}

@Composable
private fun MovieCard(
    movieItem: MovieItemUiState,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            MovieImage(
                posterPath = movieItem.posterPath,
                modifier = Modifier.fillMaxWidth()
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = movieItem.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movieItem.releaseDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MovieImage(
    posterPath: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(posterPath)
            .size(150)
            .crossfade(true)
            .build(),
        contentDescription = "Affiche du film",
        contentScale = ContentScale.Crop,
        modifier = modifier.aspectRatio(0.66f),
        placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
        error = painterResource(id = android.R.drawable.ic_dialog_alert)
    )
}



