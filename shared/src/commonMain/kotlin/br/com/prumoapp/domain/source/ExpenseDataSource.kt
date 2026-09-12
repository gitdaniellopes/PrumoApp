package br.com.prumoapp.domain.source

import br.com.prumoapp.domain.model.expense.Expense
import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.RecurringExpense
import br.com.prumoapp.domain.model.expense.RepeatType
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.YearMonth

interface ExpenseDataSource {

    fun generateNewId(userId: UserId): ExpenseId

    fun observeExpensesByMonth(
        userId: UserId,
        yearMonth: YearMonth,
        repeatType: Set<RepeatType>
    ): Flow<Result<List<Expense>>>

    fun observeRecurringExpenses(
        userId: UserId,
        yearMonth: YearMonth
    ): Flow<Result<List<RecurringExpense>>>

    fun observeExpenseById(
        userId: UserId,
        expenseId: ExpenseId
    ): Flow<Result<Expense>>

    suspend fun getExpenseById(
        userId: UserId,
        expenseId: ExpenseId
    ): Result<Expense>

    suspend fun getExpensesByParentId(
        userId: UserId,
        parentId: ExpenseId
    ): Result<List<Expense>>

    suspend fun addExpense(
        userId: UserId,
        expense: Expense
    ): Result<ExpenseId>

    suspend fun addExpenseBatch(
        userId: UserId,
        expenses: List<Expense>
    ): Result<Unit>

    suspend fun updateExpense(
        userId: UserId,
        expenseId: ExpenseId,
        expense: Expense
    ): Result<Unit>

    suspend fun updateExpensesBatch(
        userId: UserId,
        expenses: List<Expense>,
        template: Expense
    ): Result<Unit>

    suspend fun deleteExpense(
        userId: UserId,
        expenseId: ExpenseId
    ): Result<Unit>

    suspend fun deleteExpensesBatch(
        userId: UserId,
        expenseIds: List<ExpenseId>
    ): Result<Unit>

    suspend fun toggleExpensePaid(
        userId: UserId,
        expenseId: ExpenseId
    ): Result<Boolean>
}