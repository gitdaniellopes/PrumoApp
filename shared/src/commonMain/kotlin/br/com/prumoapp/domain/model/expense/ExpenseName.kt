package br.com.prumoapp.domain.model.expense

import kotlin.jvm.JvmInline

@JvmInline
value class ExpenseName(val name: String) {
    init {
        require(name.isNotBlank()) {
            "ExpenseName não pode estar em branco"
        }
    }
}