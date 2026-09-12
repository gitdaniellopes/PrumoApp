package br.com.prumoapp.domain.usecases.expense

import br.com.prumoapp.domain.model.expense.Expense
import br.com.prumoapp.domain.model.expense.FixedInstanceExpense
import br.com.prumoapp.domain.model.expense.InstallmentExpense
import br.com.prumoapp.domain.model.expense.RecurringExpense
import br.com.prumoapp.domain.model.expense.StandardExpense
import br.com.prumoapp.domain.repository.AuthRepository
import br.com.prumoapp.domain.repository.FixedExpenseRepository
import br.com.prumoapp.domain.repository.InstallmentSeriesRepository
import br.com.prumoapp.domain.repository.StandardExpenseRepository
import br.com.prumoapp.domain.repository.requireUserId

interface TogglePaidUseCase {
    suspend operator fun invoke(expense: Expense): Result<Boolean>

}

class TogglePaidUseCaseImpl(
    private val authRepository: AuthRepository,
    private val standardExpenseRepository: StandardExpenseRepository,
    private val fixedExpenseRepository: FixedExpenseRepository,
    private val installmentSeriesRepository: InstallmentSeriesRepository
) : TogglePaidUseCase {

    override suspend fun invoke(expense: Expense): Result<Boolean> = runCatching {
        val userId = authRepository.requireUserId().getOrThrow()
        when (expense) {
            is StandardExpense -> standardExpenseRepository.togglePaid(userId, expense.id)
                .getOrThrow()

            is FixedInstanceExpense -> fixedExpenseRepository.togglePainOnInstance(
                userId,
                expense.id
            ).getOrThrow()

            is InstallmentExpense -> installmentSeriesRepository.togglePaid(userId, expense.id)
                .getOrThrow()

            is RecurringExpense -> throw IllegalArgumentException(
                "Não é possivel marcar pago em um template de despesa fiza - materialize antes"
            )
        }
    }
}