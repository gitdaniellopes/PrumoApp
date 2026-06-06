package br.com.prumoapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.com.prumoapp.di.dataSourceModule
import br.com.prumoapp.di.repositoryModule
import br.com.prumoapp.di.useCaseModule
import br.com.prumoapp.di.viewModelModule
import br.com.prumoapp.ui.theme.PrumoAppTheme
import org.koin.compose.KoinApplication
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

        }
    })
}