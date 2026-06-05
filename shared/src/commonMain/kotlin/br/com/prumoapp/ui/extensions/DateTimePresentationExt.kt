package br.com.prumoapp.ui.extensions

import br.com.prumoapp.core.logging.AppLogger
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

fun YearMonth.toDisplayString(): String {
    val monthName = when (month) {
        Month.JANUARY -> "Janeiro"
        Month.FEBRUARY -> "Fevereiro"
        Month.MARCH -> "Março"
        Month.APRIL -> "Abril"
        Month.MAY -> "Maio"
        Month.JUNE -> "Junho"
        Month.JULY -> "Julho"
        Month.AUGUST -> "Agosto"
        Month.SEPTEMBER -> "Setembro"
        Month.OCTOBER -> "Outubro"
        Month.NOVEMBER -> "Novembro"
        Month.DECEMBER -> "Dezembro"
    }
    return "$monthName $year"
}

fun LocalDate.formatBrazilian(): String {
    val day = day.toString().padStart(2, '0')
    val month = (month.ordinal + 1).toString().padStart(2, '0')
    return "$day/$month/$year"
}

fun String.toLocalDateBrazilian(): LocalDate? = try {
    val parts = trim().split("/")
    if (parts.size != 3) null
    else LocalDate(year = parts[2].toInt(), month = parts[1].toInt(), day = parts[0].toInt())
} catch (e: Exception) {
    AppLogger.e("DateExtensions", "Falha ao converter string para data: \"$this\"", e)
    null
}

fun Long.toBrazilDateFormat(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDate = instant.toLocalDateTime(TimeZone.UTC).date
    return localDate.formatBrazilian()
}

fun Instant.formatBrazilian(): String =
    toLocalDateTime(TimeZone.currentSystemDefault()).date.formatBrazilian()

fun getCurrentGreeting(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    val hour = clock.now().toLocalDateTime(timeZone).hour
    return when (hour) {
        in 0..11 -> "Bom dia,"
        in 12..17 -> "Boa tarde,"
        else -> "Boa noite,"
    }
}