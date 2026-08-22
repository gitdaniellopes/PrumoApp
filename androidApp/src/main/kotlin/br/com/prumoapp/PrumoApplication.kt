package br.com.prumoapp

import android.app.Application
import br.com.prumoapp.core.logging.AppLogger

class PrumoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            AppLogger.initializedDebug()
        }
    }
}