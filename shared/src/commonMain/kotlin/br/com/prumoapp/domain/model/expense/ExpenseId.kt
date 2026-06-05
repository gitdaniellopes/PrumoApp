package br.com.prumoapp.domain.model.expense

import kotlin.jvm.JvmInline

@JvmInline
value class ExpenseId(val id: String) {

    val isAssigned: Boolean get() = id.isNotBlank()

    companion object {
        val UNASSIGNED = ExpenseId("")
    }
}