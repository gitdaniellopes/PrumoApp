package br.com.prumoapp.data.repository

import br.com.prumoapp.domain.billing.ExpenseMonthProjector
import br.com.prumoapp.domain.common.ResultFlow
import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.FixedInstanceExpense
import br.com.prumoapp.domain.model.expense.RecurringExpense
import br.com.prumoapp.domain.model.expense.RepeatType
import br.com.prumoapp.domain.model.user.UserId
import br.com.prumoapp.domain.repository.FixedExpenseRepository
import br.com.prumoapp.domain.source.ExpenseDataSource
import kotlinx.coroutines.flow.map
import kotlinx.datetime.YearMonth

class FixedExpenseRepositoryImpl(
    private val dataSource: ExpenseDataSource
) : FixedExpenseRepository {

    //Configuração recorrente

    override fun observeTemplateById(
        userId: UserId,
        expenseId: ExpenseId
    ): ResultFlow<RecurringExpense?> =
        dataSource.observeExpenseById(userId, expenseId).map { result ->
            result.mapCatching { expense ->
                expense as? RecurringExpense
                    ?: throw ClassCastException("Despesa não é do tipo RecurringExpense")
            }
        }

    override fun observeTemplatesByMonth(
        userId: UserId,
        yearMonth: YearMonth
    ): ResultFlow<List<RecurringExpense>> =
        dataSource.observeRecurringExpenses(userId, yearMonth).map { result ->
            result.map { expenses ->
                expenses.map { template ->
                    ExpenseMonthProjector.projectToMonth(template, yearMonth)
                }
            }
        }

    override suspend fun addTemplate(
        userId: UserId,
        template: RecurringExpense
    ): Result<ExpenseId> = dataSource.addExpense(userId, template)

    override suspend fun updateTemplate(
        userId: UserId,
        template: RecurringExpense
    ): Result<Unit> = dataSource.updateExpense(userId, template.id, template)

    override suspend fun deleteTemplate(
        userId: UserId,
        templateId: ExpenseId
    ): Result<Unit> = dataSource.deleteExpense(userId, templateId)

    //Configuração fixa - Ocorrencia Mensal

    override fun observeInstanceById(
        userId: UserId,
        expenseId: ExpenseId
    ): ResultFlow<FixedInstanceExpense?> =
        dataSource.observeExpenseById(userId, expenseId).map { result ->
            result.mapCatching { expense ->
                expense as? FixedInstanceExpense
                    ?: throw ClassCastException("Despesa não é do tipo FixedInstanceExpense")
            }
        }

    override fun observeInstancesByMonth(
        userId: UserId,
        yearMonth: YearMonth
    ): ResultFlow<List<FixedInstanceExpense>> = dataSource.observeExpensesByMonth(
        userId, yearMonth, setOf(RepeatType.FIXED_INSTANCE)
    ).map { result ->
        result.map { expenses -> expenses.filterIsInstance<FixedInstanceExpense>() }
    }

    override suspend fun materialize(
        userId: UserId,
        templateId: ExpenseId,
        yearMonth: YearMonth,
        paid: Boolean
    ): Result<ExpenseId> = runCatching {
        val template = dataSource.getExpenseById(userId, templateId).getOrThrow()
        if (template !is RecurringExpense) {
            throw IllegalArgumentException("Despesa não é do tipo RecurringExpense")
        }
        dataSource.addExpense(userId, template.materializeFor(yearMonth, paid)).getOrThrow()
    }

    override suspend fun updateInstance(
        userId: UserId,
        instance: FixedInstanceExpense
    ): Result<Unit> = dataSource.updateExpense(userId, instance.id, instance)

    override suspend fun deleteInstance(
        userId: UserId,
        instanceId: ExpenseId
    ): Result<Unit> = dataSource.deleteExpense(userId, instanceId)

    override suspend fun togglePainOnInstance(
        userId: UserId,
        expenseId: ExpenseId
    ): Result<Boolean> = dataSource.toggleExpensePaid(userId, expenseId)

    override suspend fun getInstanceByTemplate(
        userId: UserId,
        templateId: ExpenseId
    ): Result<List<FixedInstanceExpense>> =
        dataSource.getExpensesByParentId(userId, templateId).map { expenses ->
            expenses.filterIsInstance<FixedInstanceExpense>()
        }
}