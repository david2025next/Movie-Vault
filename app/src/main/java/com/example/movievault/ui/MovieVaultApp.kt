package com.example.movievault.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.movievault.ui.components.AppNavRail
import com.example.movievault.ui.navigation.MovaDestinations
import kotlinx.coroutines.launch


@Composable
fun MovieVaultApp(widthSizeClass: WindowWidthSizeClass) {

    val coroutineScope = rememberCoroutineScope()

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
    val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen)
    // current destination , MovaAppState [navigator,
    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                drawerState = sizeAwareDrawerState,
                navigateToHome = {},
                navigateToFavorites = {},
                closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } },
                currentDestination = MovaDestinations.HomeNavKey
            )
        },
        modifier = Modifier,
        drawerState = sizeAwareDrawerState,
        gesturesEnabled = !isExpandedScreen
    ) {
        Row {
            if (isExpandedScreen) {
                AppNavRail(
                    navigateToHome = {},
                    navigateToFavorites = {}
                )
            }
            MovieVaultContent(
                openDrawer = { coroutineScope.launch { sizeAwareDrawerState.open() } }
            )
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