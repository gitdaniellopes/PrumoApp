package br.com.prumoapp

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.prumoapp.di.dataSourceModule
import br.com.prumoapp.di.repositoryModule
import br.com.prumoapp.di.useCaseModule
import br.com.prumoapp.di.viewModelModule
import br.com.prumoapp.ui.navigation.AuthenticatedNavHost
import br.com.prumoapp.ui.navigation.UnauthenticatedNavHost
import br.com.prumoapp.ui.theme.PrumoAppTheme
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App() {
    KoinApplication(configuration = koinConfiguration(declaration = {
        modules(
            dataSourceModule,
            repositoryModule,
            useCaseModule,
            viewModelModule
        )
    }), content = {

        PrumoAppTheme {
            val mainViewModel = koinViewModel<MainViewModel>()
            val authStatus by mainViewModel.authStates.collectAsStateWithLifecycle()

            Crossfade(
                targetState = authStatus,
                animationSpec = tween(durationMillis = 600),
                label = "AuthTransition"
            ) { status ->
                when (status) {
                    is MainViewModel.AuthStatus.Authenticated -> {
                        AuthenticatedNavHost()
                    }

                    is MainViewModel.AuthStatus.Unauthenticated -> {
                        UnauthenticatedNavHost()
                    }
                }
            }
        }
    })
}