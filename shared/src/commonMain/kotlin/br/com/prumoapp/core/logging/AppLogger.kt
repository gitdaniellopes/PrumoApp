package br.com.prumoapp.core.logging

import io.github.aakira.napier.Napier

object AppLogger {
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Napier.e(message, throwable, tag)
    }

    fun d(tag: String, message: String) {
        Napier.d(message, tag = tag)
    }

    fun i(tag: String, message: String) {
        Napier.i(message, tag = tag)
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        Napier.w(message, throwable, tag)
    }
}