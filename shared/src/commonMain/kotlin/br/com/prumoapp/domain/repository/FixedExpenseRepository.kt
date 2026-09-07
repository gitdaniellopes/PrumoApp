package br.com.prumoapp.domain.repository

import br.com.prumoapp.domain.common.ResultFlow
import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.FixedInstanceExpense
import br.com.prumoapp.domain.model.expense.RecurringExpense
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.datetime.YearMonth

interface FixedExpenseRepository {

    fun observeTemplateById(userId: UserId, expenseId: ExpenseId): ResultFlow<RecurringExpense?>
    fun observeTemplatesByMonth(
        userId: UserId,
        yearMonth: YearMonth
    ): ResultFlow<List<RecurringExpense>>

    suspend fun ddTemplate(userId: UserId, template: RecurringExpense): Result<ExpenseId>
    suspend fun updateTemplate(userId: UserId, template: RecurringExpense): Result<Unit>
    suspend fun deleteTemplate(userId: UserId, templateId: ExpenseId): Result<Unit>

    fun observeInstanceById(userId: UserId, expenseId: ExpenseId): ResultFlow<FixedInstanceExpense?>
    fun observeInstancesByMonth(
        userId: UserId,
        yearMonth: YearMonth
    ): ResultFlow<List<FixedInstanceExpense>>

    suspend fun materialize(
        userId: UserId,
        templateId: ExpenseId,
        yearMonth: YearMonth,
        paid: Boolean = false,
    ): Result<ExpenseId>

    suspend fun updateInstance(userId: UserId, instance: FixedInstanceExpense): Result<Unit>
    suspend fun deleteInstance(userId: UserId, instanceId: ExpenseId): Result<Unit>
    suspend fun togglePainOnInstance(userId: UserId, expenseId: ExpenseId): Result<Boolean>
    suspend fun getInstanceByTemplate(
        userId: UserId,
        templateId: ExpenseId
    ): Result<List<FixedInstanceExpense>>
}