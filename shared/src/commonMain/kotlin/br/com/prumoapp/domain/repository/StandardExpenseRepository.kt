package br.com.prumoapp.domain.repository

import br.com.prumoapp.domain.common.ResultFlow
import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.StandardExpense
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.datetime.YearMonth

interface StandardExpenseRepository {
    fun observeById(userId: UserId, expenseId: ExpenseId): ResultFlow<StandardExpense?>
    fun observeByMonth(userId: UserId, yearMonth: YearMonth): ResultFlow<List<StandardExpense>>
    suspend fun add(userId: UserId, expense: StandardExpense): Result<ExpenseId>
    suspend fun update(userId: UserId, expense: StandardExpense): Result<Unit>
    suspend fun delete(userId: UserId, expenseId: ExpenseId): Result<Unit>
    suspend fun togglePaid(userId: UserId, expenseId: ExpenseId): Result<Boolean>
}