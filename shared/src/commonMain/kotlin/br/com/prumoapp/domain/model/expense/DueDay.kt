package br.com.prumoapp.domain.model.expense

import kotlin.jvm.JvmInline

@JvmInline
value class DueDay(val dueDay: Int) {
    init {
        require(dueDay in 1..31) {
            "O dia do vencimento deve estar entre 1 e 31"
        }
    }
}