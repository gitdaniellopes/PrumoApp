package br.com.prumoapp.data.repository

import br.com.prumoapp.domain.common.ResultFlow
import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.RepeatType
import br.com.prumoapp.domain.model.expense.StandardExpense
import br.com.prumoapp.domain.model.user.UserId
import br.com.prumoapp.domain.repository.StandardExpenseRepository
import br.com.prumoapp.domain.source.ExpenseDataSource
import kotlinx.coroutines.flow.map
import kotlinx.datetime.YearMonth

class StandardExpenseRepositoryImpl(
    private val dataSource: ExpenseDataSource
) : StandardExpenseRepository {

    override fun observeById(
        userId: UserId,
        expenseId: ExpenseId
    ): ResultFlow<StandardExpense?> =
        dataSource.observeExpenseById(userId, expenseId).map { result ->
            result.mapCatching { expense ->
                expense as? StandardExpense
                    ?: throw ClassCastException("Despesa não é do tipo StandardExpense")
            }
        }

    override fun observeByMonth(
        userId: UserId,
        yearMonth: YearMonth
    ): ResultFlow<List<StandardExpense>> =
        dataSource.observeExpensesByMonth(userId, yearMonth, setOf(RepeatType.NONE))
            .map { result ->
                result.map { expenses ->
                    expenses.filterIsInstance<StandardExpense>()
                }
            }

    override suspend fun add(
        userId: UserId,
        expense: StandardExpense
    ): Result<ExpenseId> = dataSource.addExpense(userId, expense)

    override suspend fun update(
        userId: UserId,
        expense: StandardExpense
    ): Result<Unit> = dataSource.updateExpense(userId, expense.id, expense)

    override suspend fun delete(
        userId: UserId,
        expenseId: ExpenseId
    ): Result<Unit> = dataSource.deleteExpense(userId, expenseId)

    override suspend fun togglePaid(
        userId: UserId,
        expenseId: ExpenseId
    ): Result<Boolean> = dataSource.toggleExpensePaid(userId, expenseId)
}