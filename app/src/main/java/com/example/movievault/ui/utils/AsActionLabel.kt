package com.example.movievault.ui.utils

import androidx.compose.runtime.Composable
import com.example.movievault.domain.DataErrors

@Composable
fun DataErrors.asActionLabel() : String? =
    when(this) {
        DataErrors.NetworkErrors.NO_INTERNET -> this.asUiText().asString()
        else -> null
    }