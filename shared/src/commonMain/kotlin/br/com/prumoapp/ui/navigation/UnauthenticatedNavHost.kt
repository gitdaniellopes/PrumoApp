package br.com.prumoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.prumoapp.ui.features.auth.login.LoginScreen
import br.com.prumoapp.ui.features.auth.login.LoginViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object LoginDestination

@Composable
fun UnauthenticatedNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {
        composable<LoginDestination> {
            val viewModel = koinViewModel<LoginViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LoginScreen(
                uiState = uiState,
                sideLoginEffect = viewModel.loginEffect,
                onEvent = viewModel::onEvent
            )
        }
    }
}