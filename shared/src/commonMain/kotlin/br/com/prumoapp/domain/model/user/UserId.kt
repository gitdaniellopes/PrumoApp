package br.com.prumoapp.domain.model.user

import kotlin.jvm.JvmInline

@JvmInline
value class UserId(val id: String) {
    val isAssigned: Boolean get() = id.isNotBlank()

    companion object {
        val UNASSIGNED = UserId("")
    }
}