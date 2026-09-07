package br.com.prumoapp.domain.billing

import br.com.prumoapp.domain.extensions.time.atDay
import br.com.prumoapp.domain.extensions.time.lengthOfMonth
import br.com.prumoapp.domain.model.expense.DueDay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

object DueDateCalculator {

    fun calculate(dueDay: DueDay, targetMonth: YearMonth): LocalDate {
        val lastDayMonth = targetMonth.lengthOfMonth()
        val adjustedDay = minOf(dueDay.dueDay, lastDayMonth)
        return targetMonth.atDay(adjustedDay)
    }

    fun adjustToMonth(originalDate: LocalDate, targetMonth: YearMonth): LocalDate {
        return calculate(DueDay(originalDate.day), targetMonth)
    }
}