package com.education.notes.application

import android.app.Application
import com.education.notes.di.notesModule
import com.education.notes.di.tasksModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@Application)
            modules(notesModule, tasksModule)
        }
    }
}