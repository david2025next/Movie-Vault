package com.example.movievault.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.movievault.navigation.MovaDestinations

fun EntryProviderScope<NavKey>.movieDetailEntry(onBack: () -> Unit) {
    entry<MovaDestinations.MovieDetail> {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Movie details ")
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onBack
            ) {
                Text("Back")
            }
        }
    }
}