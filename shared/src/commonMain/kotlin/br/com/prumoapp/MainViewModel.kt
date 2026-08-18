package br.com.prumoapp

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.prumoapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val authStates: StateFlow<AuthStatus> = authRepository.observeAuthState()
        .map { authUser ->
            if (authUser == null) AuthStatus.Unauthenticated else AuthStatus.Authenticated
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = getInitialAuthStatus()
        )

    fun logout() = viewModelScope.launch {
        authRepository.signOut()
    }

    private fun getInitialAuthStatus(): AuthStatus {
        val user = authRepository.currentUser
        return if (user == null) AuthStatus.Unauthenticated else AuthStatus.Authenticated
    }

    @Immutable
    sealed class AuthStatus {
        data object Authenticated : AuthStatus()
        data object Unauthenticated : AuthStatus()
    }
}