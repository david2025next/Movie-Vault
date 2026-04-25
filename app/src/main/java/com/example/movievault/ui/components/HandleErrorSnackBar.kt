package com.example.movievault.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.movievault.R
import com.example.movievault.domain.DataErrors
import com.example.movievault.ui.utils.UiText
import com.example.movievault.ui.utils.asUiText

@Composable
fun HandleErrorSnackBar(
    message : String,
    actionLabel : String ?,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onResetError: () -> Unit,
    onRetry: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        val snackBarResult = onShowSnackbar(message, actionLabel)
        if (snackBarResult) {
            onRetry()
        }
        onResetError()
    }
}