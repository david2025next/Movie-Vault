package com.example.movievault.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.example.movievault.R
import com.example.movievault.navigation.MovaDestinations
import com.example.movievault.ui.theme.MovieVaultTheme

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    currentDestination: NavKey,
    closeDrawer: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToFavorites: () -> Unit,
    modifier: Modifier = Modifier
) {

    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = modifier
    ) {
        MovieVaultLogo(modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp))
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.home)) },
            icon = { Icon(painterResource(R.drawable.ic_home), contentDescription = null) },
            selected = currentDestination == MovaDestinations.MovieListNavKey,
            onClick = {
                navigateToHome()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(R.string.favorite)) },
            icon = { Icon(painterResource(R.drawable.ic_favorite), null) },
            selected = currentDestination == MovaDestinations.FavoritesNavKey,
            onClick = {
                navigateToFavorites()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
fun MovieVaultLogo(modifier: Modifier = Modifier) {

    Row(
        modifier
    ) {
        Icon(
            painterResource(R.drawable.ic_movie_vault_icon),
            null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text(
            stringResource(R.string.app_name), style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview("Draw contents")
@Preview("Draw contents (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AppDrawerPreview(modifier: Modifier = Modifier) {
    MovieVaultTheme {
        AppDrawer(
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            currentDestination = MovaDestinations.MovieListNavKey,
            navigateToHome = {},
            navigateToFavorites = {},
            closeDrawer = {}
        )
    }
}