package com.example.movievault.ui.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movievault.R
import com.example.movievault.data.model.Actor
import com.example.movievault.data.model.Movie
import com.example.movievault.ui.components.DynamicImageAsync
import com.example.movievault.ui.components.HandleErrorSnackBar
import com.example.movievault.ui.utils.asActionLabel
import com.example.movievault.ui.utils.asUiText


@Composable
fun MovieDetailScreen(
    onShowSnackbar: suspend (message: String, action: String?) -> Boolean,
    onBack: () -> Unit,
    movieDetailViewModel: MovieDetailViewModel
) {

    val uiState by movieDetailViewModel.uiState.collectAsStateWithLifecycle()
    val error by movieDetailViewModel.errorUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    error?.let {
        HandleErrorSnackBar(
            message = it.asUiText().asString(),
            actionLabel = it.asActionLabel(),
            onRetry = movieDetailViewModel::syncActors,
            onResetError = movieDetailViewModel::onResetError,
            onShowSnackbar = onShowSnackbar
        )
    }

    when (uiState) {
        MovieDetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is MovieDetailUiState.Success -> {
            val movie = (uiState as MovieDetailUiState.Success).data.movie
            MovieDetailScreen(
                movie = movie,
                actors = (uiState as MovieDetailUiState.Success).data.actors,
                onBack = onBack,
                onToggleFavorite = { movieDetailViewModel.onToggleFavorites(!movie.isFavorite) },
                onShare = {
                    Toast.makeText(
                        context,
                        "Feature not implemented yet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}


@Composable
private fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    movie: Movie,
    actors: List<Actor>,
    onBack: () -> Unit = {},
    onToggleFavorite: () -> Unit = {},
    onShare: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box {
            DynamicImageAsync(
                model = movie.posterPath,
                Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            )
            TopBackButton(onBack = onBack)
        }

        Column(
            Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(top = 24.dp, bottom = 32.dp)
                ) {
                    HeaderSection(
                        title = movie.title,
                        popularityScore = movie.popularity
                    )

                    ActionButtonsSection(
                        isFavorite = movie.isFavorite,
                        onToggleFavorite = onToggleFavorite,
                        onShare = onShare
                    )

                    SynopsisSection(
                        synopsis = movie.overview
                    )

                    CastSection(actors)
                }
            }
        }
    }
}

@Composable
private fun TopBackButton(onBack: () -> Unit) {
    IconButton(
        onClick = onBack,
        modifier = Modifier
            .padding(top = 12.dp, start = 16.dp)
            .background(
                color = Color.Black.copy(alpha = 0.4f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
        )
    }
}

@Composable
private fun HeaderSection(title: String, popularityScore: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(16.dp))

        PopularityIndicator(score = popularityScore)
    }
}

@Composable
private fun PopularityIndicator(score: Double) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(56.dp)
    ) {
        CircularProgressIndicator(
            progress = { score.toFloat() / 100f },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 4.dp,
        )
        Text(
            text = "${score.toInt()}%",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun ActionButtonsSection(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        FilledTonalIconToggleButton(
            checked = isFavorite,
            onCheckedChange = { onToggleFavorite() }
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Add to favorite",
                tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        FilledTonalIconButton(onClick = onShare) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share"
            )
        }
    }
}

@Composable
private fun SynopsisSection(synopsis: String) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Synopsis",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = synopsis,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2f
        )
    }
}

@Composable
private fun CastSection(actors: List<Actor>) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = "Acteurs",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(actors) { actor ->
                CastMemberItem(actor)
            }
        }
    }
}

@Composable
private fun CastMemberItem(
    actor: Actor,
) {
    Column(
        modifier = Modifier.width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DynamicImageAsync(
            actor.profilePath, Modifier
                .size(100.dp)
                .clip(CircleShape),
            placeholder = painterResource(R.drawable.ic_default_user_profile)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = actor.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = actor.character,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview("content (dark)", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "content light")
@Composable
private fun MovieDetailScreenPreview() {
    MaterialTheme {
        MovieDetailScreen(
            movie = Movie(
                1,
                "Eternel infernal",
                "2024-04-27",
                "poster",
                23.0,
                24,
                true,
                "la mere des dragons est deja tres en colere parce qu'elle a perdu ses enfants",
                34.4
            ),
            actors = listOf(
                Actor(
                    name = "Charles",
                    profilePath = "profile path",
                    character = "Voix de micheal"
                ),
                Actor(
                    name = "Dave",
                    profilePath = "profile path",
                    character = "Voix de tariq"
                ), Actor(
                    name = "Charles",
                    profilePath = "profile path",
                    character = "Voix de micheal"
                ),
                Actor(
                    name = "Dave",
                    profilePath = "profile path",
                    character = "Voix de tariq"
                ),
                Actor(
                    name = "Charles",
                    profilePath = "profile path",
                    character = "Voix de micheal"
                ),
                Actor(
                    name = "Dave",
                    profilePath = "profile path",
                    character = "Voix de tariq"
                ),
                Actor(
                    name = "Charles",
                    profilePath = "profile path",
                    character = "Voix de micheal"
                ),
                Actor(
                    name = "Dave",
                    profilePath = "profile path",
                    character = "Voix de tariq"
                ),
                Actor(
                    name = "Charles",
                    profilePath = "profile path",
                    character = "Voix de micheal"
                ),
                Actor(
                    name = "Dave",
                    profilePath = "profile path",
                    character = "Voix de tariq"
                )
            )
        )
    }
}