package br.com.prumoapp.data.repository

import br.com.prumoapp.domain.model.common.Money
import br.com.prumoapp.domain.model.user.User
import br.com.prumoapp.domain.model.user.UserId
import br.com.prumoapp.domain.repository.UserRepository
import br.com.prumoapp.domain.source.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override fun observerUser(userId: UserId): Flow<Result<User>> {
        return userRemoteDataSource.observerUser(userId)
    }

    override suspend fun createUser(user: User): Result<Unit> {
        return userRemoteDataSource.createUser(userId = user.id, user = user)
    }

    override suspend fun updateMonthlyBudget(
        userId: UserId,
        budget: Money
    ): Result<Unit> {
        return userRemoteDataSource.updateMonthlyBudget(userId, budget.cents / 100.0)
    }
}