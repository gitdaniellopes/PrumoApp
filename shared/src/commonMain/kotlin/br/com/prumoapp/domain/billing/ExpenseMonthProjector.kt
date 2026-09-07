package br.com.prumoapp.domain.billing

import br.com.prumoapp.domain.model.expense.RecurringExpense
import kotlinx.datetime.YearMonth

internal object ExpenseMonthProjector {

    fun projectToMonth(
        expense: RecurringExpense,
        targetMonth: YearMonth
    ): RecurringExpense = expense.copy(
        startDate = DueDateCalculator.adjustToMonth(expense.startDate, targetMonth)
    )
}