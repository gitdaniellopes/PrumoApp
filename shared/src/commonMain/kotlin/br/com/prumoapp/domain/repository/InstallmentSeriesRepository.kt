package br.com.prumoapp.domain.repository

import br.com.prumoapp.domain.common.ResultFlow
import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.InstallmentExpense
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.datetime.YearMonth

interface InstallmentSeriesRepository {

    fun generatedNewParentId(userId: UserId): ExpenseId
    fun observeById(userId: UserId, expenseId: ExpenseId): ResultFlow<InstallmentExpense>
    fun observeByMonth(userId: UserId, yearMonth: YearMonth): ResultFlow<List<InstallmentExpense>>
    suspend fun addSeries(userId: UserId, installments: List<InstallmentExpense>): Result<Unit>
    suspend fun getSeries(userId: UserId, parentId: ExpenseId): Result<List<InstallmentExpense>>
    suspend fun updateSeries(
        userId: UserId,
        toUpdate: List<InstallmentExpense>,
        template: InstallmentExpense
    ): Result<Unit>

    suspend fun deleteSeries(userId: UserId, expenseIds: List<ExpenseId>): Result<Unit>
    suspend fun togglePaid(userId: UserId, expenseId: ExpenseId): Result<Boolean>
}