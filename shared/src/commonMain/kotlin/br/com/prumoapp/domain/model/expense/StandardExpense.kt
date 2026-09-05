package br.com.prumoapp.domain.model.expense

import br.com.prumoapp.domain.model.common.Money
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

data class StandardExpense(
    override val id: ExpenseId,
    override val userId: UserId,
    override val name: ExpenseName,
    override val amount: Money,
    override val parentExpenseId: ExpenseId,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val dueDate: LocalDate,
    val paymentStatus: PaymentStatus = PaymentStatus.Pending,
    val notes: String? = null
) : Expense {
    override val kind: ExpenseKind = ExpenseKind.STANDARD

    override fun withUserId(newUserId: UserId): Expense = copy(userId = newUserId)
}
