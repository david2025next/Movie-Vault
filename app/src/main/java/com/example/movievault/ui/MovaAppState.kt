package com.example.movievault.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.example.movievault.navigation.MovaDestinations
import com.example.movievault.navigation.NavigationState
import com.example.movievault.navigation.rememberNavigationState

@Composable
fun rememberMovaAppState(): MovaAppState {

    val navigationState = rememberNavigationState(MovaDestinations.MovieListNavKey, TOP_LEVEL_NAV_ITEMS.keys)
    val showTopAppBar = remember(navigationState) { navigationState.backStacks[navigationState.topLevelRoute]?.last() != MovaDestinations.MovieDetail }

    Log.d("TAG", "rememberMovaAppState: $showTopAppBar")
    return remember(navigationState) {
        MovaAppState(
            navigationState = navigationState,
            showTopAppBar = showTopAppBar
        )
    }
}

@Stable
class MovaAppState(
    val navigationState: NavigationState,
    val showTopAppBar: Boolean
)