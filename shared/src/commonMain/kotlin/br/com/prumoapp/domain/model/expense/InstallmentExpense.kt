package br.com.prumoapp.domain.model.expense

import br.com.prumoapp.domain.model.common.Money
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

data class InstallmentExpense(
    override val id: ExpenseId,
    override val userId: UserId,
    override val name: ExpenseName,
    override val amount: Money,
    override val parentExpenseId: ExpenseId? = null,
    override val createdAt: Instant? = null,
    override val updatedAt: Instant? = null,
    override val kind: ExpenseKind,
    override val dueDate: LocalDate,
    val installmentNumber: InstallmentNumber,
    val payment: PaymentStatus = PaymentStatus.Pending,
    val repeatType: RepeatType = RepeatType.INSTALLMENT
) : Expense {
    override fun withUserId(newUserId: UserId): Expense = copy(userId = newUserId)
}
