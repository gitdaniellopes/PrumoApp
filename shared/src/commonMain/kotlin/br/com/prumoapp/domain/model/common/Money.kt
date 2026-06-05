package br.com.prumoapp.domain.model.common

import kotlin.jvm.JvmInline
import kotlin.math.roundToLong

@JvmInline
value class Money(val cents: Long) {

    companion object {
        val ZERO = Money(0L)
        fun of(value: Double): Money = Money((value * 100).roundToLong())

        fun of(value: String): Money {
            val normalized = value.trim().replace(",", ".")
            val isNegative = normalized.startsWith("-")
            val absStr = if (isNegative) normalized.removePrefix("-") else normalized
            val dotIndex = absStr.indexOf('.')
            val absCents = if (dotIndex < 0) {
                (absStr.toLongOrNull() ?: 0L) * 100L
            } else {
                val intPart = absStr.substring(0, dotIndex).toLongOrNull() ?: 0L
                val decStr = absStr.substring(dotIndex + 1).take(2).padEnd(2, '0')
                val decPart = decStr.toLongOrNull() ?: 0L
                intPart * 100L + decPart
            }
            return Money(if (isNegative) -absCents else absCents)
        }
    }

    operator fun plus(other: Money): Money = Money(this.cents + other.cents)
    operator fun minus(other: Money): Money = Money(this.cents - other.cents)
    operator fun times(multiplier: Int): Money = Money((this.cents * multiplier))
    operator fun times(multiplier: Double): Money = Money((this.cents * multiplier).roundToLong())

    operator fun div(divisor: Int): Money = Money((cents.toDouble() / divisor).roundToLong())
    operator fun div(divisor: Double): Money = Money((this.cents / divisor).roundToLong())

    operator fun compareTo(other: Money): Int = this.cents.compareTo(other.cents)

    fun isPositiveOrZero(): Boolean = cents >= 0L

    fun toDisplayString(): String {
        val absCents = if (cents < 0) -cents else cents
        val sign = if (cents < 0) "-" else ""
        val integerPart = absCents / 100
        val decimalPart = absCents % 100
        return "$sign$integerPart,${decimalPart.toString().padStart(2, '0')}"
    }

    override fun toString(): String {
        val absCents = if (cents < 0) -cents else cents
        val sign = if (cents < 0) "-" else ""
        val integerPart = absCents / 100
        val decimalPart = absCents % 100
        return "$sign$integerPart.${decimalPart.toString().padStart(2, '0')}"
    }
}