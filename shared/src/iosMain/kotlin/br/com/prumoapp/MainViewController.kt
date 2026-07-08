package br.com.prumoapp

import androidx.compose.ui.window.ComposeUIViewController
import br.com.prumoapp.data.auth.GoogleSignInHelper
import br.com.prumoapp.data.auth.GoogleSignInRegistry

fun MainViewController(googleSignInHelper: GoogleSignInHelper) = ComposeUIViewController {
    GoogleSignInRegistry.helper = googleSignInHelper
    App()
}