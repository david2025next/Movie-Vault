package com.example.movievault.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.movievault.ui.components.AppNavRail
import com.example.movievault.navigation.MovaDestinations
import com.example.movievault.navigation.Navigator
import kotlinx.coroutines.launch


val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState state should be initialized at runtime")
}

@Composable
fun MovieVaultApp(movaAppState: MovaAppState, widthSizeClass: WindowWidthSizeClass) {

    val coroutineScope = rememberCoroutineScope()

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
    val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen)

    val navigator = remember { Navigator(movaAppState.navigationState) }

    val snackbarHostState = remember { SnackbarHostState() }

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                drawerState = sizeAwareDrawerState,
                navigateToHome = { navigator.navigate(MovaDestinations.MovieListNavKey) },
                navigateToFavorites = { navigator.navigate(MovaDestinations.FavoritesNavKey) },
                closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } },
                currentDestination = movaAppState.navigationState.topLevelRoute
            )
        },
        modifier = Modifier,
        drawerState = sizeAwareDrawerState,
        gesturesEnabled = !isExpandedScreen
    ) {
        Row {
            if (isExpandedScreen) {
                AppNavRail(
                    navigateToHome = { navigator.navigate(MovaDestinations.MovieListNavKey) },
                    navigateToFavorites = { navigator.navigate(MovaDestinations.FavoritesNavKey) }
                )
            }


            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                MovieVaultContent(
                    showTopAppBar = movaAppState.showTopAppBar,
                    navigator = navigator,
                    openDrawer = { coroutineScope.launch { sizeAwareDrawerState.open() } }
                )
            }
        }
    }
}

@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    return if (!isExpandedScreen) {
        drawerState
    } else
        DrawerState(initialValue = DrawerValue.Closed)
}