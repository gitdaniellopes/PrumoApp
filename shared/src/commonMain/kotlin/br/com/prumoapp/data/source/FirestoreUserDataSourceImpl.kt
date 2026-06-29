package br.com.prumoapp.data.source

import br.com.prumoapp.core.logging.AppLogger
import br.com.prumoapp.data.mapper.toDomain
import br.com.prumoapp.data.mapper.toFirestore
import br.com.prumoapp.data.model.UserFirestore
import br.com.prumoapp.data.source.firebase.USERS_COLLECTIONS
import br.com.prumoapp.domain.model.user.User
import br.com.prumoapp.domain.model.user.UserId
import br.com.prumoapp.domain.source.UserRemoteDataSource
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val TAG = "FirestoreUserDataSource"

class FirestoreUserDataSourceImpl(
    private val firestore: FirebaseFirestore
) : UserRemoteDataSource {

    private fun getDocumentRef(userId: UserId) =
        firestore.collection(USERS_COLLECTIONS).document(userId.id)

    override fun observerUser(userId: UserId): Flow<Result<User>> {
        return getDocumentRef(userId).snapshots
            .map { documentSnapshot ->
                if (!documentSnapshot.exists) throw Exception("Usuario não encontrado")
                val userResponse = documentSnapshot.data<UserFirestore>().toDomain(userId.id)
                Result.success(userResponse)
            }
            .catch { error ->
                AppLogger.e(TAG, "Erro ao observar usuario id=${userId.id}", error)
                emit(Result.failure(error))
            }
    }

    override suspend fun getUser(userId: UserId): Result<User> {
        return try {
            val doc = getDocumentRef(userId).get()
            if (!doc.exists) return Result.failure(Exception("Usuario não encontrado"))
            val userResponse = doc.data<UserFirestore>().toDomain(userId.id)
            Result.success(userResponse)
        } catch (e: Exception) {
            AppLogger.e(TAG, "Erro ao obter usuario id=${userId.id}", e)
            Result.failure(e)
        }
    }

    override suspend fun createUser(
        userId: UserId,
        user: User
    ): Result<Unit> {
        return try {
            getDocumentRef(userId).set(user.toFirestore())
            Result.success(Unit)
        } catch (e: Exception) {
            AppLogger.e(TAG, "Erro ao criar usuario id=${userId.id}", e)
            Result.failure(e)
        }
    }

    override suspend fun updateMonthlyBudget(
        userId: UserId,
        budget: Double
    ): Result<Unit> {
        return try {
            getDocumentRef(userId).update(mapOf("monthlyBudget" to budget))
            Result.success(Unit)
        } catch (e: Exception) {
            AppLogger.e(TAG, "Erro ao atualizar orçamento mensal do usuario id=${userId.id}", e)
            Result.failure(e)
        }
    }
}