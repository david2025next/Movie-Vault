package com.example.movievault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movievault.ui.home.HomeScreen
import com.example.movievault.ui.home.HomeViewModel
import com.example.movievault.ui.theme.MovieVaultTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel : HomeViewModel by  viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.isLoading }
        setContent {
            MovieVaultTheme {
                HomeScreen()
            }
        }
    }
}