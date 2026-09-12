package br.com.prumoapp.data.repository

import br.com.prumoapp.domain.common.ResultFlow
import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.InstallmentExpense
import br.com.prumoapp.domain.model.expense.RepeatType
import br.com.prumoapp.domain.model.user.UserId
import br.com.prumoapp.domain.repository.InstallmentSeriesRepository
import br.com.prumoapp.domain.source.ExpenseDataSource
import kotlinx.coroutines.flow.map
import kotlinx.datetime.YearMonth

class InstallmentSeriesRepositoryImpl(
    private val dataSource: ExpenseDataSource
) : InstallmentSeriesRepository {

    override fun generatedNewParentId(userId: UserId): ExpenseId = dataSource.generateNewId(userId)

    override fun observeById(
        userId: UserId,
        expenseId: ExpenseId
    ): ResultFlow<InstallmentExpense> =
        dataSource.observeExpenseById(userId, expenseId).map { result ->
            result.mapCatching { expense ->
                expense as? InstallmentExpense
                    ?: throw ClassCastException("Despesa não é do tipo InstallmentExpense")
            }
        }


    override fun observeByMonth(
        userId: UserId,
        yearMonth: YearMonth
    ): ResultFlow<List<InstallmentExpense>> = dataSource.observeExpensesByMonth(
        userId, yearMonth, setOf(RepeatType.INSTALLMENT)
    ).map { result ->
        result.map { expenses ->
            expenses.filterIsInstance<InstallmentExpense>()
        }
    }

    override suspend fun addSeries(
        userId: UserId,
        installments: List<InstallmentExpense>
    ): Result<Unit> = dataSource.addExpenseBatch(userId, installments)

    override suspend fun getSeries(
        userId: UserId,
        parentId: ExpenseId
    ): Result<List<InstallmentExpense>> =
        dataSource.getExpensesByParentId(userId, parentId).map { expenses ->
            expenses.filterIsInstance<InstallmentExpense>()
        }

    override suspend fun updateSeries(
        userId: UserId,
        toUpdate: List<InstallmentExpense>,
        template: InstallmentExpense
    ): Result<Unit> = dataSource.updateExpensesBatch(userId, toUpdate, template)

    override suspend fun deleteSeries(
        userId: UserId,
        expenseIds: List<ExpenseId>
    ): Result<Unit> = dataSource.deleteExpensesBatch(userId, expenseIds)

    override suspend fun togglePaid(
        userId: UserId,
        expenseId: ExpenseId
    ): Result<Boolean> = dataSource.toggleExpensePaid(userId, expenseId)
}