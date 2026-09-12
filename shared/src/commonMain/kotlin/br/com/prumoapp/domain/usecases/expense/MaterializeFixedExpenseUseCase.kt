package br.com.prumoapp.domain.usecases.expense

import br.com.prumoapp.domain.model.expense.ExpenseId
import br.com.prumoapp.domain.repository.AuthRepository
import br.com.prumoapp.domain.repository.FixedExpenseRepository
import br.com.prumoapp.domain.repository.requireUserId
import kotlinx.datetime.YearMonth

interface MaterializeFixedExpenseUseCase {
    suspend operator fun invoke(
        templateId: ExpenseId,
        yearMonth: YearMonth,
        paid: Boolean = false
    ): Result<ExpenseId>
}

class MaterializeFixedExpenseUseCaseImpl(
    private val authRepository: AuthRepository,
    private val fixedExpenseRepository: FixedExpenseRepository
) : MaterializeFixedExpenseUseCase {

    override suspend fun invoke(
        templateId: ExpenseId,
        yearMonth: YearMonth,
        paid: Boolean
    ): Result<ExpenseId> = runCatching {
        val userId = authRepository.requireUserId().getOrThrow()
        fixedExpenseRepository.materialize(userId, templateId, yearMonth, paid).getOrThrow()
    }
}