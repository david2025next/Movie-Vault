package com.example.movievault.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.movievault.R
import com.example.movievault.domain.DataErrors

@Composable
fun DataErrors.asActionLabel() : String? =
    when(this) {
        DataErrors.NetworkErrors.NO_INTERNET, DataErrors.NetworkErrors.TIMEOUT -> stringResource(R.string.action_retry)
        else -> null
    }