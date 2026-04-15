package com.example.movievault.ui.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.movievault.R
import com.example.movievault.domain.DataErrors
import com.example.movievault.ui.utils.UiText
import com.example.movievault.ui.utils.asUiText

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by homeViewModel.isRefreshing.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    uiState.error?.let {
        HandleError(it, snackBarHostState, homeViewModel::onRefresh)
    }
    HomeScreen(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        isRefreshing = isRefreshing,
        onRefresh = homeViewModel::onRefresh
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    snackBarHostState: SnackbarHostState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {}
) {

    Scaffold(
        modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) })
    { paddingValues ->

        val state = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = state
                )
            }
        ) {

            if(uiState.isLoading){
                LoadingScreen()
            } else {
                when(uiState) {
                    is HomeUiState.HasMovies ->{
                        MoviesList(uiState.items)
                    }
                    is HomeUiState.NoMovies ->{
                        EmptyScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text("Movies empty")
    }
}
@Composable
private fun HandleError(
    errors: DataErrors,
    snackBarHostState: SnackbarHostState,
    onRefresh: () -> Unit
) {
    val message = errors.asUiText().asString()
    val actionLabel =
        if (errors == DataErrors.NetworkErrors.TIMEOUT || errors == DataErrors.NetworkErrors.NO_INTERNET) {
            UiText.StringResource(R.string.action_retry).asString()
        } else null
    LaunchedEffect(errors) {
        val result = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = actionLabel == null
        )
        when (result) {
            SnackbarResult.Dismissed -> {}
            SnackbarResult.ActionPerformed -> onRefresh()
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
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            MovieCard(movie)
        }
    }
}

@Composable
private fun MovieCard(
    movieItem: MovieItemUiState,
    modifier: Modifier = Modifier
) {

    Column(modifier.fillMaxWidth()) {
        MovieImage(movieItem.posterPath, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = movieItem.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "⭐ ${movieItem.voteAverage}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "(${movieItem.voteCount})",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(text = movieItem.releaseDate, style = MaterialTheme.typography.bodyMedium)
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
            .crossfade(true)
            .build(),
        contentDescription = "Affiche du film",
        contentScale = ContentScale.Crop,
        modifier = modifier.aspectRatio(16f / 9f),
        placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
        error = painterResource(id = android.R.drawable.ic_dialog_alert)
    )
}



