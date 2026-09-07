package br.com.prumoapp.domain.model.expense

import br.com.prumoapp.domain.billing.DueDateCalculator
import br.com.prumoapp.domain.model.common.Money
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlin.time.Clock
import kotlin.time.Instant

data class RecurringExpense(
    override val id: ExpenseId,
    override val userId: UserId,
    override val name: ExpenseName,
    override val amount: Money,
    override val parentExpenseId: ExpenseId? = null,
    override val createdAt: Instant? = null,
    override val updatedAt: Instant? = null,
    val startDate: LocalDate,
    val dueDay: DueDay,
    val repeatType: RepeatType = RepeatType.FIXED
) : Expense {

    override val kind: ExpenseKind = ExpenseKind.FIXED_TEMPLATE
    override val dueDate: LocalDate
        get() = startDate

    override fun withUserId(newUserId: UserId): Expense = copy(userId = newUserId)

    fun materializeFor(
        yearMonth: YearMonth,
        paid: Boolean = false,
        now: Instant = Clock.System.now()
    ): FixedInstanceExpense {
        val dueDate = DueDateCalculator.calculate(dueDay, yearMonth)

        return FixedInstanceExpense(
            id = ExpenseId.UNASSIGNED,
            userId = userId,
            name = name,
            amount = amount,
            parentExpenseId = id,
            createdAt = now,
            updatedAt = null,
            dueDate = dueDate,
            paymentStatus = if (paid) {
                PaymentStatus.Paid(now)
            } else {
                PaymentStatus.Pending
            }
        )
    }
}
