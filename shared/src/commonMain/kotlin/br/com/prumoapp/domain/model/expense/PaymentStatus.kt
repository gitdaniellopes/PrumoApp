package br.com.prumoapp.domain.model.expense

import kotlin.time.Instant

sealed class PaymentStatus {
    data object Pending : PaymentStatus()
    data class Paid(val at: Instant) : PaymentStatus()
}

val PaymentStatus.isPaid: Boolean
    get() = this is PaymentStatus.Paid

val PaymentStatus.paidAt: Instant?
    get() = (this as? PaymentStatus.Paid)?.at