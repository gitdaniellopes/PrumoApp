package br.com.prumoapp.domain.usecases.expense

import br.com.prumoapp.domain.model.expense.Expense
import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.model.expense.ExpenseScope
import br.com.prumoapp.domain.model.expense.FixedInstanceExpense
import br.com.prumoapp.domain.model.expense.InstallmentExpense
import br.com.prumoapp.domain.model.expense.RecurringExpense
import br.com.prumoapp.domain.model.expense.StandardExpense
import br.com.prumoapp.domain.model.user.UserId
import br.com.prumoapp.domain.repository.AuthRepository
import br.com.prumoapp.domain.repository.FixedExpenseRepository
import br.com.prumoapp.domain.repository.InstallmentSeriesRepository
import br.com.prumoapp.domain.repository.StandardExpenseRepository
import br.com.prumoapp.domain.repository.requireUserId

interface DeleteExpenseUseCase {
    suspend operator fun invoke(parameters: Parameters): Result<Unit>
    data class Parameters(val expense: Expense? = null, val scope: ExpenseScope)
}

class DeleteExpenseUseCaseImpl(
    private val authRepository: AuthRepository,
    private val standardExpenseRepository: StandardExpenseRepository,
    private val fixedExpenseRepository: FixedExpenseRepository,
    private val installmentSeriesRepository: InstallmentSeriesRepository
) : DeleteExpenseUseCase {

    override suspend fun invoke(parameters: DeleteExpenseUseCase.Parameters): Result<Unit> =
        runCatching {
            val userId = authRepository.requireUserId().getOrThrow()
            val expense =
                parameters.expense ?: throw IllegalArgumentException("Despesa não encontrada")
            val scope = parameters.scope

            when (expense) {
                is StandardExpense -> {
                    require(scope == ExpenseScope.THIS_ONLY) {
                        "StandardExpense só admite escopo THIS_ONLY (recebido $scope)"
                    }
                    standardExpenseRepository.delete(userId, expense.id).getOrThrow()
                }

                is RecurringExpense -> {
                    require(scope == ExpenseScope.ALL_SERIES) {
                        "RecurringExpense só admite escopo ALL_SERIES (recebido $scope)"
                    }
                    deleteFixedAggregate(userId, expense.id)
                }

                is FixedInstanceExpense -> when (scope) {
                    ExpenseScope.THIS_ONLY -> {
                        fixedExpenseRepository.deleteInstance(userId, expense.id).getOrThrow()
                    }

                    ExpenseScope.ALL_SERIES -> {
                        val templateId = expense.parentExpenseId
                            ?: throw IllegalArgumentException("TemplateId não encontrado")
                        deleteFixedAggregate(userId, templateId)
                    }

                    ExpenseScope.THIS_AND_FUTURE -> throw IllegalArgumentException(
                        "FixedInstanceExpense não admintre escopo THIS_AND_FUTURE"
                    )
                }

                is InstallmentExpense -> when (scope) {
                    ExpenseScope.THIS_ONLY -> {
                        installmentSeriesRepository.deleteSeries(userId, listOf(expense.id))
                            .getOrThrow()
                    }

                    ExpenseScope.THIS_AND_FUTURE -> {
                        val parentId = expense.parentExpenseId
                            ?: throw IllegalArgumentException("TemplateId não encontrado")
                        val series =
                            installmentSeriesRepository.getSeries(userId, parentId).getOrThrow()
                        val futureIds =
                            series.filter { it.dueDate >= expense.dueDate }.map { it.id }
                        installmentSeriesRepository.deleteSeries(userId, futureIds).getOrThrow()
                    }

                    ExpenseScope.ALL_SERIES -> {
                        val parentId = expense.parentExpenseId
                            ?: throw IllegalArgumentException("TemplateId não encontrado")
                        val series =
                            installmentSeriesRepository.getSeries(userId, parentId).getOrThrow()
                        val expenseIds = series.map { it.id }
                        installmentSeriesRepository.deleteSeries(userId, expenseIds).getOrThrow()
                    }
                }
            }
        }

    private suspend fun deleteFixedAggregate(userId: UserId, templateId: ExpenseId) {
        val instances =
            fixedExpenseRepository.getInstanceByTemplate(userId, templateId).getOrThrow()
        instances.forEach { instance ->
            fixedExpenseRepository.deleteInstance(userId, instance.id).getOrThrow()
        }
        fixedExpenseRepository.deleteTemplate(userId, templateId).getOrThrow()
    }
}