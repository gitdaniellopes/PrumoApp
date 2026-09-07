package br.com.prumoapp.domain.billing

import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.InstallmentExpense
import br.com.prumoapp.domain.model.expense.InstallmentNumber
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlin.time.Clock
import kotlin.time.Instant

object InstallmentScheduler {

    fun schedulerFrom(
        baseExpense: InstallmentExpense,
        parentId: ExpenseId,
        now: Instant = Clock.System.now()
    ): List<InstallmentExpense> {
        val total = baseExpense.installmentNumber.total

        return (0 until total).map { index ->
            baseExpense.copy(
                id = ExpenseId.UNASSIGNED,
                parentExpenseId = parentId,
                installmentNumber = InstallmentNumber(
                    current = index + 1,
                    total = total
                ),
                dueDate = baseExpense.dueDate.plus(index, DateTimeUnit.MONTH),
                createdAt = now,
                updatedAt = null
            )
        }
    }
}