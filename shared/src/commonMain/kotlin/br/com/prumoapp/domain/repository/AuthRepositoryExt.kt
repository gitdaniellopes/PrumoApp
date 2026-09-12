package br.com.prumoapp.domain.repository

import br.com.prumoapp.domain.model.user.UserId

fun AuthRepository.requireUserId(): Result<UserId> = currentUser?.uid?.let {
    Result.success(it)
} ?: Result.failure(IllegalStateException("Usuário não autenticado"))