package br.com.prumoapp.domain.source

import br.com.prumoapp.domain.model.user.User
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {

    fun observerUser(userId: UserId): Flow<Result<User>>
    suspend fun getUser(userId: UserId): Result<User>
    suspend fun createUser(userId: UserId, user: User): Result<Unit>
    suspend fun updateMonthlyBudget(userId: UserId, budget: Double): Result<Unit>
}