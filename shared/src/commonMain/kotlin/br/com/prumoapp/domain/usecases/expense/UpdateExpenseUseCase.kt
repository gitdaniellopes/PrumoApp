package br.com.prumoapp.domain.usecases.expense

import br.com.prumoapp.domain.model.expense.Expense
import br.com.prumoapp.domain.model.expense.ExpenseScope
import br.com.prumoapp.domain.model.expense.FixedInstanceExpense
import br.com.prumoapp.domain.model.expense.InstallmentExpense
import br.com.prumoapp.domain.model.expense.RecurringExpense
import br.com.prumoapp.domain.model.expense.StandardExpense
import br.com.prumoapp.domain.repository.AuthRepository
import br.com.prumoapp.domain.repository.FixedExpenseRepository
import br.com.prumoapp.domain.repository.InstallmentSeriesRepository
import br.com.prumoapp.domain.repository.StandardExpenseRepository
import br.com.prumoapp.domain.repository.requireUserId

interface UpdateExpenseUseCase {
    suspend operator fun invoke(parameters: Parameters): Result<Unit>
    data class Parameters(val expense: Expense, val scope: ExpenseScope)
}

class UpdateExpenseUseCaseImpl(
    private val authRepository: AuthRepository,
    private val standardExpenseRepository: StandardExpenseRepository,
    private val fixedExpenseRepository: FixedExpenseRepository,
    private val installmentSeriesRepository: InstallmentSeriesRepository
) : UpdateExpenseUseCase {


    override suspend fun invoke(parameters: UpdateExpenseUseCase.Parameters): Result<Unit> =
        runCatching {
            val userId = authRepository.requireUserId().getOrThrow()
            val (expense, scope) = parameters

            when (expense) {
                is StandardExpense -> {
                    require(scope == ExpenseScope.THIS_ONLY) {
                        "StandardExpense só admite escopo THIS_ONLY (recebido $scope)"
                    }
                    standardExpenseRepository.update(userId, expense).getOrThrow()
                }

                is RecurringExpense -> {
                    require(scope == ExpenseScope.THIS_ONLY) {
                        "RecurringExpense só admite escopo THIS_ONLY (recebido $scope)"
                    }
                    fixedExpenseRepository.updateTemplate(userId, expense).getOrThrow()
                }

                is FixedInstanceExpense -> {
                    require(scope == ExpenseScope.THIS_ONLY) {
                        "FixedInstanceExpense só admite escopo THIS_ONLY (recebido $scope)"
                    }
                    fixedExpenseRepository.updateInstance(userId, expense).getOrThrow()
                }

                is InstallmentExpense -> when (scope) {
                    ExpenseScope.THIS_ONLY -> {
                        installmentSeriesRepository.updateSeries(userId, listOf(expense), expense)
                            .getOrThrow()
                    }

                    ExpenseScope.THIS_AND_FUTURE -> {
                        val parentId = expense.parentExpenseId
                            ?: throw IllegalArgumentException("ParentExpenseId não encontrado")
                        val series =
                            installmentSeriesRepository.getSeries(userId, parentId).getOrThrow()
                        val future = series.filter { it.dueDate >= expense.dueDate }
                        installmentSeriesRepository.updateSeries(userId, future, expense)
                            .getOrThrow()
                    }

                    ExpenseScope.ALL_SERIES -> {
                        val parentId = expense.parentExpenseId
                            ?: throw IllegalArgumentException("ParentExpenseId não encontrado")
                        val series =
                            installmentSeriesRepository.getSeries(userId, parentId).getOrThrow()
                        installmentSeriesRepository.updateSeries(userId, series, expense)
                            .getOrThrow()
                    }
                }
            }
        }
}