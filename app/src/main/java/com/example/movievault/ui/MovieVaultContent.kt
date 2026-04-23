package com.example.movievault.ui

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.movievault.R
import com.example.movievault.navigation.MovaDestinations
import com.example.movievault.navigation.Navigator
import com.example.movievault.ui.detail.movieDetailEntry
import com.example.movievault.ui.favorites.favoritesEntry
import com.example.movievault.ui.movies.movieListEntry


@Composable
fun MovieVaultContent(navigator: Navigator, showTopAppBar: Boolean, openDrawer: () -> Unit) {

    val entryProvider = entryProvider {
        movieListEntry(
            onMovieItemClick = { id -> navigator.navigate(MovaDestinations.MovieDetail(id)) }
        )
        favoritesEntry(
            onFavoriteClick = { id -> navigator.navigate(MovaDestinations.MovieDetail(id)) }
        )
        movieDetailEntry(onBack = navigator::goBack)
    }

    val title = TOP_LEVEL_NAV_ITEMS[navigator.state.topLevelRoute]
        ?: error("error topLevelRoute ${navigator.state.topLevelRoute}")

    MovieVaultContent(
        navEntries = navigator.state.toDecoratedEntries(entryProvider),
        showTopAppBar = showTopAppBar,
        onBack = { navigator.goBack() },
        openDrawer = openDrawer,
        modifier = Modifier,
        title = title
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieVaultContent(
    navEntries: List<NavEntry<NavKey>>,
    @StringRes title: Int,
    onBack: () -> Unit,
    showTopAppBar: Boolean,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = LocalSnackbarHostState.current

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            if (showTopAppBar) {
                MovaTopAppBar(
                    title = title,
                    openDrawer = openDrawer
                )
            }
        }
    ) { paddingValues ->

        NavDisplay(
            modifier = Modifier.padding(paddingValues),
            entries = navEntries,
            onBack = onBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovaTopAppBar(
    @StringRes title: Int,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.enterAlwaysScrollBehavior(
        topAppBarState
    )
) {

    val context = LocalContext.current

    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(title))
        },
        navigationIcon = {
            IconButton(openDrawer) {
                Icon(painterResource(R.drawable.ic_movie_vault_icon), null)
            }
        },
        actions = {
            IconButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Search is not yet implemented in this configuration",
                        Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                Icon(painterResource(R.drawable.ic_search), null)
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}