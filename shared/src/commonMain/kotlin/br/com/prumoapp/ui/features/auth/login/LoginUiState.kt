package br.com.prumoapp.ui.features.auth.login

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val errorMessage: String) : LoginUiState()
}
