package br.com.prumoapp.domain.model.expense

import br.com.prumoapp.domain.model.common.Money
import br.com.prumoapp.domain.model.user.UserId
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

sealed interface Expense {

    val id: ExpenseId
    val userId: UserId
    val name: ExpenseName
    val amount: Money
    val parentExpenseId: ExpenseId?
    val createdAt: Instant?
    val updatedAt: Instant?

    val kind: ExpenseKind
    val dueDate: LocalDate

    fun withUserId(newUserId: UserId): Expense
}