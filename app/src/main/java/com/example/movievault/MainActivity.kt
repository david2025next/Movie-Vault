package com.example.movievault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.movievault.ui.MovieVaultApp
import com.example.movievault.ui.rememberMovaAppState
import com.example.movievault.ui.theme.MovieVaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MovieVaultTheme {
                val appState = rememberMovaAppState()
                val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
                MovieVaultApp(movaAppState = appState, widthSizeClass = widthSizeClass)
            }
        }
    }
}