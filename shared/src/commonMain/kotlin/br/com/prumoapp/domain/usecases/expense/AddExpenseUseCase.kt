package br.com.prumoapp.domain.usecases.expense

import br.com.prumoapp.domain.billing.InstallmentScheduler
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

interface AddExpenseUseCase {
    suspend operator fun invoke(expense: Expense): Result<Unit>
}

class AddExpenseUseCaseImpl(
    private val authRepository: AuthRepository,
    private val standardExpenseRepository: StandardExpenseRepository,
    private val fixedExpenseRepository: FixedExpenseRepository,
    private val installmentSeriesRepository: InstallmentSeriesRepository
) : AddExpenseUseCase {
    override suspend fun invoke(expense: Expense): Result<Unit> = runCatching {
        val userId = authRepository.requireUserId().getOrThrow()
        when (expense) {
            is StandardExpense -> {
                standardExpenseRepository.add(userId, expense).getOrThrow()
            }

            is InstallmentExpense -> {
                val parentId = installmentSeriesRepository.generatedNewParentId(userId)
                val series = InstallmentScheduler.schedulerFrom(expense, parentId)
                installmentSeriesRepository.addSeries(userId, series).getOrThrow()
            }

            is RecurringExpense -> {
                fixedExpenseRepository.addTemplate(userId, expense).getOrThrow()
            }

            is FixedInstanceExpense -> {
                throw IllegalArgumentException("Despesa fixa não pode ser adicionada")
            }
        }
    }
}