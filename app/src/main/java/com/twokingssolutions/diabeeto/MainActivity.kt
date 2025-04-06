package com.twokingssolutions.diabeeto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.twokingssolutions.diabeeto.di.databaseModule
import com.twokingssolutions.diabeeto.ui.theme.DiabeetoTheme
import com.twokingssolutions.diabeeto.internalStorage.cleanUpOldImages
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(databaseModule)
        }
        enableEdgeToEdge()
        cleanUpOldImages(this)
        setContent {
            DiabeetoTheme {
                MainNavigation()
            }
        }
    }
}
