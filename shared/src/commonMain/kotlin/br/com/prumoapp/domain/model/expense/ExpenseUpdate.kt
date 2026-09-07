package br.com.prumoapp.domain.model.expense

import br.com.prumoapp.domain.model.common.Money
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

data class ExpenseUpdate(
    val userId: UserId,
    val name: ExpenseName,
    val amount: Money,
    val dueDate: LocalDate,
    val paid: Boolean,
    val paidAt: Instant?,
    val repeatType: RepeatType,
    val installment: Boolean,
    val currentInstallment: Int?,
    val totalInstallments: Int?,
    val parentExpenseId: ExpenseId?,
    val dueDay: DueDay?
)
