package com.example.movievault.ui.favorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun FavoritesScreen(
    onFavoriteClick: (Int) -> Unit,
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {

}

@Composable
private fun FavoritesScreen(onFavoriteClick: (Int) -> Unit, modifier: Modifier = Modifier) {
}

@Composable
private fun FavoritesList(modifier: Modifier = Modifier, onFavoriteClick: (Int) -> Unit) {

}

@Composable
private fun FavoriteItem(onFavoriteClick: () -> Unit, modifier: Modifier = Modifier,) {

}