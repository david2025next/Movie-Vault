package com.example.movievault.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.movievault.R

@Composable
fun DynamicImageAsync(
    model: String,
    modifier: Modifier = Modifier,
    placeholder: Painter = painterResource(R.drawable.ic_placeholder_movie),
    onImageClicked: () -> Unit = {}
) {

    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val imageLoader = rememberAsyncImagePainter(
        model = model,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        }
    )

    Box(
        Modifier.clickable(
            onClick = onImageClicked
        )
    ) {
        if (isLoading) {
            Box(
                modifier.background(MaterialTheme.colorScheme.surfaceVariant)
            ) {}
        }
        Image(
            painter = if (isError.not()) imageLoader else placeholder,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}