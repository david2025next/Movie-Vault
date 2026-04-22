package com.example.movievault.ui.home

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.movievault.R
import com.example.movievault.data.model.Movie
import com.example.movievault.domain.DataErrors
import com.example.movievault.ui.utils.UiText
import com.example.movievault.ui.utils.asUiText

@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val items = homeViewModel.uiState.collectAsLazyPagingItems()
    val error by homeViewModel.error.collectAsStateWithLifecycle()
    error?.let {
        HandleError(
            error = it,
            snackBarHostState = snackBarHostState,
            onRetry = {
                homeViewModel.resetError()
                items.retry() }
        )
    }
    MoviesScreen(
        items,
        errorSnackBar = homeViewModel::errorHandler,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun HandleError(
    error: DataErrors,
    snackBarHostState: SnackbarHostState,
    onRetry: () -> Unit
) {
    val text = error.asUiText().asString()
    val actionLabel =
        if (error == DataErrors.NetworkErrors.NO_INTERNET) UiText.StringResource(R.string.action_retry)
            .asString() else null
    LaunchedEffect(key1 = Unit) {
        val result = snackBarHostState.showSnackbar(
            message = text,
            actionLabel = actionLabel
        )
        when (result) {
            SnackbarResult.Dismissed -> {}
            SnackbarResult.ActionPerformed -> onRetry()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoviesScreen(
    items: LazyPagingItems<Movie>,
    modifier: Modifier = Modifier,
    errorSnackBar: (e: Throwable) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing = items.loadState.refresh is LoadState.Loading
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        state = pullToRefreshState,
        onRefresh = { items.refresh() },
        modifier = modifier,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
                isRefreshing = isRefreshing
            )
        }
    ) {
        MoviesContent(items, errorSnackBar)
        MoviesRefreshStateHandler(items, errorSnackBar)
    }
}

@Composable
fun MoviesRefreshStateHandler(items: LazyPagingItems<Movie>, errorHandle: (e: Throwable) -> Unit) {
    when (val state = items.loadState.refresh) {
        is LoadState.Error -> {
            if (items.itemCount == 0) {
                FullScreenError()
            }
            errorHandle(state.error)
        }

        LoadState.Loading -> {
            if (items.itemCount == 0) {
                FullScreenLoading()
            }
        }

        is LoadState.NotLoading -> Unit
    }
}

@Composable
fun FullScreenError(modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Error ")
    }
}

@Composable
fun FullScreenLoading(modifier: Modifier = Modifier) {
    Column(modifier) {
        repeat(2) {
            AnimatedShimmer()
        }
    }
}

@Composable
private fun MoviesContent(items: LazyPagingItems<Movie>, errorSnackBar: (e: Throwable) -> Unit) {
    LazyColumn() {
        items(
            count = items.itemCount,
            key = items.itemKey { it.id }
        ) { index ->
            items[index]?.let { movie ->
                MovieItem(movie)
            }
        }

        item {
            MoviesAppendStateHandler(items, errorHandle = errorSnackBar)
        }
    }
}

@Composable
fun MoviesAppendStateHandler(items: LazyPagingItems<Movie>, errorHandle: (e: Throwable) -> Unit) {
    when (val state = items.loadState.append) {
        is LoadState.Error -> {
            errorHandle(state.error)
        }

        LoadState.Loading -> BottomLoading()
        is LoadState.NotLoading -> Unit
    }
}

@Composable
fun BottomLoading(modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MovieItem(
    item: Movie,
    modifier: Modifier = Modifier
) {

    Column(modifier.fillMaxWidth()) {
        MovieImage(item.posterPath, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = item.title,
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
                        text = "⭐ ${item.voteAverage}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "(${item.voteCount})",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(text = item.releaseDate, style = MaterialTheme.typography.bodyMedium)
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



