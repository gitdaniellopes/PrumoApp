package br.com.prumoapp.domain.extensions.time

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

private fun isLeapYear(year: Int): Boolean =
    (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

fun YearMonth.lengthOfMonth(): Int = when (month) {
    Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY,
    Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31

    Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
    Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
}

fun YearMonth.plusMonths(months: Int): YearMonth {
    val totalMonths = year * 12 + month.ordinal + months
    val newYear = totalMonths / 12
    val newMonthOrdinal = totalMonths % 12
    return YearMonth(newYear, Month.entries[newMonthOrdinal])
}

fun YearMonth.minusMonths(months: Int): YearMonth = plusMonths(-months)

fun YearMonth.atDay(day: Int): LocalDate = LocalDate(year, month, day)
fun YearMonth.atEndOfMonth(): LocalDate = atDay(lengthOfMonth())
fun LocalDate.toYearMonth(): YearMonth = YearMonth(year, month)

fun LocalDate.withDayOfMonth(day: Int): LocalDate {
    val maxDay = toYearMonth().lengthOfMonth()
    return LocalDate(year, month, day.coerceAtMost(maxDay))
}