package br.com.prumoapp.domain.repository

import br.com.prumoapp.domain.model.common.Money
import br.com.prumoapp.domain.model.user.User
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observerUser(userId: UserId): Flow<Result<User>>
    suspend fun createUser(user: User): Result<Unit>
    suspend fun updateMonthlyBudget(userId: UserId, budget: Money): Result<Unit>
}