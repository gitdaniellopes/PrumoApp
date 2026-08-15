package br.com.prumoapp.ui.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.prumoapp.core.logging.AppLogger
import br.com.prumoapp.domain.model.auth.AuthCredentialData
import br.com.prumoapp.domain.usecases.LoginWithGoogleUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginEffect = MutableSharedFlow<LoginEffect>()
    val loginEffect = _loginEffect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnGoogleSignInClick -> {
                launchGoogleSignIn()
            }

            is LoginEvent.OnGoogleCredentialReceived -> {
                performGoogleLogin(event.credential)
            }

            is LoginEvent.OnGoogleSignInError -> {
                _uiState.value = LoginUiState.Error(event.errorMessage)
            }

            is LoginEvent.OnGoogleSignInCancelled -> {
                _uiState.value = LoginUiState.Idle
            }

            is LoginEvent.OnGoogleSignInCompleted -> {
                _uiState.value = LoginUiState.Success
            }
        }
    }

    private fun launchGoogleSignIn() {
        if (_uiState.value is LoginUiState.Loading) return
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            _loginEffect.emit(LoginEffect.LaunchGoogleSignIn)
        }
    }

    private fun performGoogleLogin(credential: AuthCredentialData) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            AppLogger.d("LoginViewModel", "Iniciando login com google no firebase")

            loginWithGoogleUseCase(credential).fold(
                onSuccess = {
                    AppLogger.d("LoginViewModel", "Login com google realizado com sucesso")
                    _uiState.value = LoginUiState.Success
                },
                onFailure = { error ->
                    AppLogger.e("LoginViewModel", "Erro ao realizar login com google", error)
                    _uiState.value =
                        LoginUiState.Error(error.message ?: "Erro ao fazer login com o google")
                }
            )
        }
    }
}