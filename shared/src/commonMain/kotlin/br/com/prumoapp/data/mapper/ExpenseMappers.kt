package br.com.prumoapp.data.mapper

import br.com.prumoapp.core.logging.AppLogger
import br.com.prumoapp.data.extensions.toInstant
import br.com.prumoapp.data.extensions.toLocalDate
import br.com.prumoapp.data.extensions.toUtcTimestamp
import br.com.prumoapp.data.model.ExpenseFirestore
import br.com.prumoapp.domain.model.common.Money
import br.com.prumoapp.domain.model.expense.DueDay
import br.com.prumoapp.domain.model.expense.Expense
import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.ExpenseName
import br.com.prumoapp.domain.model.expense.ExpenseUpdate
import br.com.prumoapp.domain.model.expense.FixedInstanceExpense
import br.com.prumoapp.domain.model.expense.InstallmentExpense
import br.com.prumoapp.domain.model.expense.InstallmentNumber
import br.com.prumoapp.domain.model.expense.PaymentStatus
import br.com.prumoapp.domain.model.expense.RecurringExpense
import br.com.prumoapp.domain.model.expense.RepeatType
import br.com.prumoapp.domain.model.expense.StandardExpense
import br.com.prumoapp.domain.model.expense.isPaid
import br.com.prumoapp.domain.model.expense.paidAt
import br.com.prumoapp.domain.model.user.UserId
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.LocalDate
import kotlin.time.Clock

fun ExpenseFirestore.toDomain(id: String, userId: String): Expense {
    val repeatType = try {
        RepeatType.valueOf(repeatType)
    } catch (e: IllegalArgumentException) {
        AppLogger.e("ExpenseMappers", "RepeatType inválido: $repeatType")
        RepeatType.NONE
    }

    val expenseId = ExpenseId(id)
    val ownerId = UserId(userId)
    val expenseName = ExpenseName(name.ifBlank { "Sem nome" })
    val parentId = parentExpenseId?.let(::ExpenseId)
    val payment = toPaymentStatus()
    val safeInstallmentTotal = (totalInstallments ?: 2).coerceAtLeast(2)
    val safeInstallmentCurrent = (currentInstallment ?: 1).coerceIn(1, safeInstallmentTotal)

    return when (repeatType) {
        RepeatType.FIXED -> RecurringExpense(
            id = expenseId,
            userId = ownerId,
            name = expenseName,
            amount = Money.of(amount),
            parentExpenseId = parentId,
            createdAt = createdAt?.toInstant(),
            updatedAt = updatedAt?.toInstant(),
            startDate = dueDate.toLocalDate(),
            dueDay = DueDay(dueDay ?: dueDate.toLocalDate().day)
        )

        RepeatType.FIXED_INSTANCE -> FixedInstanceExpense(
            id = expenseId,
            userId = ownerId,
            name = expenseName,
            amount = Money.of(amount),
            parentExpenseId = parentId,
            createdAt = createdAt?.toInstant(),
            updatedAt = updatedAt?.toInstant(),
            dueDate = dueDate.toLocalDate(),
            paymentStatus = payment
        )

        RepeatType.INSTALLMENT -> InstallmentExpense(
            id = expenseId,
            userId = ownerId,
            name = expenseName,
            amount = Money.of(amount),
            parentExpenseId = parentId,
            createdAt = createdAt?.toInstant(),
            updatedAt = updatedAt?.toInstant(),
            dueDate = dueDate.toLocalDate(),
            installmentNumber = InstallmentNumber(
                current = safeInstallmentCurrent,
                total = safeInstallmentTotal
            ),
            payment = payment
        )

        RepeatType.NONE -> StandardExpense(
            id = expenseId,
            userId = ownerId,
            name = expenseName,
            amount = Money.of(amount),
            parentExpenseId = parentId,
            createdAt = createdAt?.toInstant(),
            updatedAt = updatedAt?.toInstant(),
            dueDate = dueDate.toLocalDate(),
            paymentStatus = payment,
        )
    }
}

fun Expense.toFirestore(): ExpenseFirestore {
    val now = createdAt?.toUtcTimestamp() ?: Timestamp.now()
    return when (this) {
        is StandardExpense -> ExpenseFirestore(
            userId = userId.id,
            name = name.name,
            amount = amount.cents.toDouble() / 100.0,
            dueDate = dueDate.toUtcTimestamp(),
            paid = paymentStatus is PaymentStatus.Paid,
            paidAt = paymentStatus.paidAt?.toUtcTimestamp(),
            repeatType = RepeatType.NONE.name,
            installment = false,
            createdAt = now,

            )

        is FixedInstanceExpense -> ExpenseFirestore(
            userId = userId.id,
            name = name.name,
            amount = amount.cents.toDouble() / 100.0,
            dueDate = dueDate.toUtcTimestamp(),
            paid = paymentStatus.isPaid,
            paidAt = paymentStatus.paidAt?.toUtcTimestamp(),
            repeatType = RepeatType.FIXED_INSTANCE.name,
            parentExpenseId = parentExpenseId?.id,
            installment = false,
            createdAt = now
        )

        is RecurringExpense -> ExpenseFirestore(
            userId = userId.id,
            name = name.name,
            amount = amount.cents.toDouble() / 100.0,
            dueDate = startDate.toUtcTimestamp(),
            repeatType = RepeatType.FIXED.name,
            parentExpenseId = parentExpenseId?.id,
            dueDay = dueDate.day,
            installment = false,
            createdAt = now
        )

        is InstallmentExpense -> ExpenseFirestore(
            userId = userId.id,
            name = name.name,
            amount = amount.cents.toDouble() / 100.0,
            dueDate = dueDate.toUtcTimestamp(),
            repeatType = RepeatType.INSTALLMENT.name,
            parentExpenseId = parentExpenseId?.id,
            paid = payment.isPaid,
            paidAt = payment.paidAt?.toUtcTimestamp(),
            installment = true,
            currentInstallment = installmentNumber.current,
            totalInstallments = installmentNumber.total,
            createdAt = now
        )
    }
}

private fun ExpenseFirestore.toPaymentStatus(): PaymentStatus =
    if (paid) {
        PaymentStatus.Paid(paidAt?.toInstant() ?: createdAt?.toInstant() ?: Clock.System.now())
    } else {
        PaymentStatus.Pending
    }

fun ExpenseUpdate.toFirebaseMap(): Map<String, Any?> = mapOf(
    "name" to name.name,
    "amount" to amount.cents / 100.0,
    "dueDate" to dueDate.toUtcTimestamp(),
    "paid" to paid,
    "paidAt" to paidAt?.toUtcTimestamp(),
    "repeatType" to repeatType.name,
    "installment" to installment,
    "currentInstallment" to currentInstallment,
    "totalInstallments" to totalInstallments,
    "parentExpenseId" to parentExpenseId?.id,
    "dueDay" to dueDay?.dueDay,
    "updatedAt" to FieldValue.serverTimestamp
)

fun Expense.tpUpdateMap(adjustedDate: LocalDate): Map<String, Any?> = mapOf(
    "name" to name.name,
    "amount" to amount.cents / 100.0,
    "dueDate" to adjustedDate.toUtcTimestamp(),
    "updatedAt" to FieldValue.serverTimestamp
)



