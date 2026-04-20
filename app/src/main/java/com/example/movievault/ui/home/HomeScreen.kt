package com.example.movievault.ui.home

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.movievault.data.model.Movie

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    val uiState = homeViewModel.uiState.collectAsLazyPagingItems()
    HomeScreen(items = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    items: LazyPagingItems<Movie>,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier,
    )
    { paddingValues ->
        MoviesScreen(
            items,
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

@Composable
private fun MoviesScreen(
    pagingItems: LazyPagingItems<Movie>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
    ) {
        MoviesContent(pagingItems)

        MoviesRefreshStateHandler(pagingItems)
    }
}

@Composable
fun MoviesContent(
    pagingItems: LazyPagingItems<Movie>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pagingItems.itemCount) { index ->
            pagingItems[index]?.let { movie ->
                MovieCard(movie)
            }
        }

        item {
            MoviesAppendStateHandler(pagingItems)
        }
    }
}

@Composable
fun MoviesRefreshStateHandler(
    pagingItems: LazyPagingItems<Movie>
) {
    when (val state = pagingItems.loadState.refresh) {

        is LoadState.Loading -> {
            if (pagingItems.itemCount == 0) {
                FullScreenLoading()
            }
        }

        is LoadState.Error -> {
            if (pagingItems.itemCount == 0) {
                FullScreenError(
                    message = state.error.message,
                    onRetry = { pagingItems.retry() }
                )
            } else {
                ErrorSnackBar(message = state.error.message)
            }
        }

        is LoadState.NotLoading -> Unit
    }
}

@Composable
fun ErrorSnackBar(message: String?) {
    val context = LocalContext.current
    Toast.makeText(
        context,
        "$message",
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun MoviesAppendStateHandler(
    pagingItems: LazyPagingItems<Movie>
) {
    when (val state = pagingItems.loadState.append) {

        is LoadState.Loading -> {
            BottomLoading()
        }

        is LoadState.Error -> {
            BottomError(
                message = state.error.message,
                onRetry = { pagingItems.retry() }
            )
        }

        is LoadState.NotLoading -> {
//            if (state.endOfPaginationReached) {
//                EndOfList()
//            }
            Unit
        }
    }
}

@Composable
fun AnimatedShimmer() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    MovieCardShimmer(brush = brush)
}

@Composable
fun FullScreenLoading() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column() {
            repeat(7){
                AnimatedShimmer()
            }
        }
    }
}

@Composable
fun FullScreenError(
    message: String?,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message ?: "Erreur")
        Button(onClick = onRetry) {
            Text("Réessayer")
        }
    }
}

@Composable
fun BottomLoading() {
    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun BottomError(
    message: String?,
    onRetry: () -> Unit
) {
    Row {
        Text(text = "Erreur")
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun EndOfList() {
    Text(
        text = "Tu es à jour",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun MovieCardShimmer(
    brush: Brush,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .background(brush)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Spacer(
                    modifier = Modifier
                        .width(100.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )

                Spacer(
                    modifier = Modifier
                        .width(60.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
            }
        }
    }
}

@Composable
private fun MovieCard(
    movieItem: Movie,
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
        modifier = modifier.aspectRatio(2f / 3f),
        placeholder = painterResource(id = android.R.drawable.ic_menu_gallery), // placeholder for skeleton
        error = painterResource(id = android.R.drawable.ic_dialog_alert)
    )
}



