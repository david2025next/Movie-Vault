package com.example.movievault.ui.movies

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.movievault.data.model.Movie
import com.example.movievault.ui.components.DynamicImageAsync
import com.example.movievault.ui.components.HandleErrorSnackBar
import com.example.movievault.ui.utils.asActionLabel
import com.example.movievault.ui.utils.asUiText

@Composable
fun MoviesListScreen(
    onMovieItemClicked: (Int) -> Unit,
    onShowSnackbar: suspend (message: String, action: String?) -> Boolean,
    viewModel: MoviesViewModel = hiltViewModel()
) {

    val items = viewModel.uiState.collectAsLazyPagingItems()
    val error by viewModel.error.collectAsStateWithLifecycle()

    error?.let {
        HandleErrorSnackBar(
            message = it.asUiText().asString(),
            actionLabel = it.asActionLabel(),
            onShowSnackbar,
            onRetry = { items.retry() },
            onResetError = viewModel::resetError
        )
    }

    MoviesListScreen(
        items = items,
        onMovieItemClicked = onMovieItemClicked,
        onError = viewModel::errorHandler,
        modifier = Modifier.fillMaxSize()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoviesListScreen(
    items: LazyPagingItems<Movie>,
    onMovieItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onError: (e: Throwable) -> Unit
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
        MoviesContent(
            items = items,
            onError = onError,
            onMovieItemClicked = onMovieItemClicked
        )
        MoviesRefreshStateHandler(items.itemCount, items.loadState.refresh, onError)
    }
}

@Composable
fun MoviesRefreshStateHandler(
    itemCount: Int,
    state: LoadState,
    onError: (e: Throwable) -> Unit
) {
    when (state) {
        is LoadState.Error -> {
            if (itemCount == 0) {
                FullScreenError()
            }
            onError(state.error)
        }

        LoadState.Loading -> {
            if (itemCount == 0) {
                FullScreenLoading()
            }
        }

        is LoadState.NotLoading -> Unit
    }
}

@Composable
fun FullScreenError(modifier: Modifier = Modifier) {
    /*
    show full screen empty movie and error to load
     */
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
            MovieItemShimmer()
        }
    }
}

@Composable
private fun MoviesContent(
    items: LazyPagingItems<Movie>,
    onMovieItemClicked: (Int) -> Unit,
    onError: (e: Throwable) -> Unit
) {

    val state: LazyListState = rememberLazyListState()

    LazyColumn(
        state = state
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey { it.id }
        ) { index ->
            items[index]?.let { movie ->
                MovieItem(movie, onMovieItemClicked = { onMovieItemClicked(movie.id) })
            }
        }

        item {
            MoviesAppendStateHandler(items.loadState.append, onError = onError)
        }
    }
}

@Composable
fun MoviesAppendStateHandler(state: LoadState, onError: (e: Throwable) -> Unit) {
    when (state) {
        is LoadState.Error -> {
            onError(state.error)
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
    onMovieItemClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier
            .fillMaxWidth()
    ) {
        DynamicImageAsync(
            model = item.posterPath,
            onImageClicked = onMovieItemClicked,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
        )
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


@Preview(showBackground = true)
@Composable
fun MovieItemShimmer(
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
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
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Spacer(
                    modifier = Modifier
                        .width(100.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                Spacer(
                    modifier = Modifier
                        .width(60.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        }
    }
}



