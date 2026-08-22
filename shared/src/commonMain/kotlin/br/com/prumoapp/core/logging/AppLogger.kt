package br.com.prumoapp.core.logging

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object AppLogger {

    private var initialized = false

    fun initializedDebug() {
        if (initialized) return

        Napier.base(DebugAntilog())
        initialized = true
    }

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