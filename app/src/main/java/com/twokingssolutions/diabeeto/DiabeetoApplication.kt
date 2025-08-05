package com.twokingssolutions.diabeeto

import android.app.Application
import com.twokingssolutions.diabeeto.di.appModule
import com.twokingssolutions.diabeeto.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DiabeetoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DiabeetoApplication)
            modules(
                listOf(
                    databaseModule,
                    appModule
                )
            )
        }
    }
}