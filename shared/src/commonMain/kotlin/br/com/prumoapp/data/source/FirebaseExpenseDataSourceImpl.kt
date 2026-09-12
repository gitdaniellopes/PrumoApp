package br.com.prumoapp.data.source

import br.com.prumoapp.core.logging.AppLogger
import br.com.prumoapp.data.extensions.toUtcTimestamp
import br.com.prumoapp.data.mapper.toDomain
import br.com.prumoapp.data.mapper.toFirestore
import br.com.prumoapp.data.mapper.toFirestoreUpdateMap
import br.com.prumoapp.data.model.ExpenseFirestore
import br.com.prumoapp.data.source.firebase.EXPENSES_COLLECTIONS
import br.com.prumoapp.data.source.firebase.USERS_COLLECTIONS
import br.com.prumoapp.domain.extensions.time.atDay
import br.com.prumoapp.domain.extensions.time.atEndOfMonth
import br.com.prumoapp.domain.extensions.time.withDayOfMonth
import br.com.prumoapp.domain.model.expense.Expense
import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.RecurringExpense
import br.com.prumoapp.domain.model.expense.RepeatType
import br.com.prumoapp.domain.model.user.UserId
import br.com.prumoapp.domain.source.ExpenseDataSource
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.YearMonth

private const val TAG = "FirebaseExpenseDataSource"

class FirebaseExpenseDataSourceImpl(
    private val firestore: FirebaseFirestore
) : ExpenseDataSource {

    private fun getCollectionRef(userId: UserId) =
        firestore.collection(USERS_COLLECTIONS)
            .document(userId.id)
            .collection(EXPENSES_COLLECTIONS)

    override fun generateNewId(userId: UserId): ExpenseId =
        ExpenseId(getCollectionRef(userId).document.id)


    override fun observeExpensesByMonth(
        userId: UserId,
        yearMonth: YearMonth,
        repeatType: Set<RepeatType>
    ): Flow<Result<List<Expense>>> {
        require(repeatType.isNotEmpty()) {
            "Infome ao metos um RepeatType válido"
        }

        val startTs = yearMonth.atDay(1).toUtcTimestamp()
        val endTs = yearMonth.atEndOfMonth().toUtcTimestamp()

        return getCollectionRef(userId)
            .where { "dueDate" greaterThanOrEqualTo startTs }
            .where { "dueDate" lessThanOrEqualTo endTs }
            .where {
                "repeatType" inArray repeatType.map(RepeatType::name)
            }
            .snapshots
            .map { snapshot ->
                val expenses = snapshot.documents.map { doc ->
                    doc.data<ExpenseFirestore>().toDomain(doc.id, userId.id)
                }
                Result.success(expenses)
            }
            .catch {
                AppLogger.e(TAG, "Erro ao observar despesas do mes: $yearMonth", it)
                emit(Result.failure(it))
            }
    }

    override fun observeRecurringExpenses(
        userId: UserId,
        yearMonth: YearMonth
    ): Flow<Result<List<RecurringExpense>>> {

        val endTs = yearMonth.atEndOfMonth().toUtcTimestamp()

        return getCollectionRef(userId)
            .where { "repeatType" equalTo RepeatType.FIXED.name }
            .where { "dueDate" lessThanOrEqualTo endTs }
            .snapshots
            .map { snapshot ->
                val recurringExpenses = snapshot.documents.mapNotNull { doc ->
                    doc.data<ExpenseFirestore>()
                        .toDomain(doc.id, userId.id) as? RecurringExpense
                }
                Result.success(recurringExpenses)
            }
            .catch {
                AppLogger.e(TAG, "Erro ao observar despesas recorrentes do mes: $yearMonth", it)
                emit(Result.failure(it))
            }

    }

    override fun observeExpenseById(
        userId: UserId,
        expenseId: ExpenseId
    ): Flow<Result<Expense>> {
        return getCollectionRef(userId).document(expenseId.id).snapshots
            .map { doc ->
                if (!doc.exists) {
                    throw NoSuchElementException("Despesa não encontrada")
                }
                val expense = doc.data<ExpenseFirestore>().toDomain(doc.id, userId.id)
                Result.success(expense)
            }
            .catch {
                AppLogger.e(TAG, "Erro ao observar despesa: $expenseId", it)
                emit(Result.failure(it))
            }
    }

    override suspend fun getExpenseById(
        userId: UserId,
        expenseId: ExpenseId
    ): Result<Expense> = try {

        val doc = getCollectionRef(userId).document(expenseId.id).get()
        if (!doc.exists) {
            return Result.failure(NoSuchElementException("Despesa não encontrada"))
        }
        val expense = doc.data<ExpenseFirestore>().toDomain(doc.id, userId.id)
        Result.success(expense)

    } catch (e: Exception) {
        AppLogger.e(TAG, "Erro ao buscar despesa: $expenseId", e)
        Result.failure(e)
    }

    override suspend fun getExpensesByParentId(
        userId: UserId,
        parentId: ExpenseId
    ): Result<List<Expense>> = try {
        val snapshot = getCollectionRef(userId)
            .where { "parentExpenseId" equalTo parentId.id }
            .get()

        val expenses = snapshot.documents.map { doc ->
            doc.data<ExpenseFirestore>().toDomain(doc.id, userId.id)
        }
        Result.success(expenses)

    } catch (e: Exception) {
        AppLogger.e(TAG, "Erro ao buscar despesas do pai: ${parentId.id}", e)
        Result.failure(e)
    }

    override suspend fun addExpense(
        userId: UserId,
        expense: Expense
    ): Result<ExpenseId> = try {
        val docRef = getCollectionRef(userId)
            .add(expense.withUserId(userId).toFirestore())
        AppLogger.d(TAG, "Despesa adicionada com sucesso: ${docRef.id}")

        Result.success(ExpenseId(docRef.id))
    } catch (e: Exception) {
        AppLogger.e(TAG, "Erro ao adicionar despesa: $expense", e)
        Result.failure(e)
    }

    override suspend fun addExpenseBatch(
        userId: UserId,
        expenses: List<Expense>
    ): Result<Unit> = try {
        val batch = firestore.batch()
        val collectionRef = getCollectionRef(userId)
        expenses.forEach { expense ->
            batch.set(collectionRef.document, expense.withUserId(userId).toFirestore())
        }
        batch.commit()
        Result.success(Unit)
    } catch (e: Exception) {
        AppLogger.e(TAG, "Erro ao adicionar despesas em lote", e)
        Result.failure(e)
    }

    override suspend fun updateExpense(
        userId: UserId,
        expenseId: ExpenseId,
        expense: Expense
    ): Result<Unit> = try {
        val updateMap = expense.toFirestoreUpdateMap()
        getCollectionRef(userId).document(expenseId.id).update(updateMap)
        Result.success(Unit)
    } catch (e: Exception) {
        AppLogger.e(TAG, "Erro ao atualizar despesas: ${expenseId.id}", e)
        Result.failure(e)
    }

    override suspend fun updateExpensesBatch(
        userId: UserId,
        expenses: List<Expense>,
        template: Expense
    ): Result<Unit> = try {
        val batch = firestore.batch()
        val collectionRef = getCollectionRef(userId)
        val templateDay = template.dueDate.day

        expenses.forEach { expense ->
            val currentDueDate = if (expense is RecurringExpense) null else expense.dueDate
            if (currentDueDate != null) {
                val adjustedDate = currentDueDate.withDayOfMonth(templateDay)
                val updateMap = expense.toFirestoreUpdateMap(adjustedDate)
                batch.update(collectionRef.document(expense.id.id), updateMap)
            }
        }
        batch.commit()
        Result.success(Unit)
    } catch (e: Exception) {
        AppLogger.e(TAG, "Erro ao atualizar despesas em lote", e)
        Result.failure(e)
    }

    override suspend fun deleteExpense(
        userId: UserId,
        expenseId: ExpenseId
    ): Result<Unit> = try {
        getCollectionRef(userId).document(expenseId.id).delete()
        Result.success(Unit)
    } catch (e: Exception) {
        AppLogger.e(TAG, "Erro ao deletar despesa: ${expenseId.id}", e)
        Result.failure(e)
    }

    override suspend fun deleteExpensesBatch(
        userId: UserId,
        expenseIds: List<ExpenseId>
    ): Result<Unit> = try {
        val batch = firestore.batch()
        val collectionRef = getCollectionRef(userId)
        expenseIds.forEach { expenseId ->
            batch.delete(collectionRef.document(expenseId.id))
        }
        batch.commit()
        Result.success(Unit)
    } catch (e: Exception) {
        AppLogger.e(TAG, "Erro ao deletar despesas em lote", e)
        Result.failure(e)
    }

    override suspend fun toggleExpensePaid(
        userId: UserId,
        expenseId: ExpenseId
    ): Result<Boolean> = try {
        val docRef = getCollectionRef(userId).document(expenseId.id)
        val newPaid = firestore.runTransaction {
            val snapshot = get(docRef)
            if (!snapshot.exists) {
                throw NoSuchElementException("Despesa não encontrada")
            }

            val expenseData = snapshot.data<ExpenseFirestore>()
            val paid = !expenseData.paid

            update(
                docRef, mapOf(
                    "paid" to paid,
                    "paidAt" to if (paid) FieldValue.serverTimestamp else null,
                    "updatedAt" to FieldValue.serverTimestamp
                )
            )
            paid
        }
        Result.success(newPaid)

    } catch (e: Exception) {
        AppLogger.e(TAG, "Erro ao alternar statusa de pagamento da despesa: ${expenseId.id}", e)
        Result.failure(e)
    }
}