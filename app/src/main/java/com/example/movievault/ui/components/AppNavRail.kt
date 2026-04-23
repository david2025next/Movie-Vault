package com.example.movievault.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movievault.R
import com.example.movievault.ui.theme.MovieVaultTheme

@Composable
fun AppNavRail(
    navigateToHome: () -> Unit,
    navigateToFavorites: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        header = {
            Icon(
                painterResource(R.drawable.ic_movie_vault_icon),
                null, Modifier.padding(vertical = 12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    ) {
        Spacer(Modifier.weight(1f))
        NavigationRailItem(
            selected = true,
            label = { Text(stringResource(R.string.home)) },
            icon = { Icon(painterResource(R.drawable.ic_home), stringResource(R.string.home)) },
            onClick = navigateToHome
        )
        NavigationRailItem(
            selected = false,
            label = {Text(stringResource(R.string.favorite))},
            icon = {Icon(painterResource(R.drawable.ic_favorite), stringResource(R.string.favorite))},
            onClick = navigateToFavorites
        )
        Spacer(Modifier.weight(1f))
    }
}

@Preview("rail contents")
@Preview("rail contents (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AppNavRailPreview() {
    MovieVaultTheme {
        AppNavRail(
            navigateToHome = {},
            navigateToFavorites = {}
        )
    }
}