package com.twokingssolutions.diabeeto

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.twokingssolutions.diabeeto.di.databaseModule
import com.twokingssolutions.diabeeto.ui.theme.DiabeetoTheme
import com.twokingssolutions.diabeeto.internalStorage.cleanUpOldImages

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    private var isSplashScreenVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                isSplashScreenVisible
            }
            setOnExitAnimationListener { splashScreenViewProvider ->
                val translateY = ObjectAnimator.ofFloat(
                    splashScreenViewProvider.iconView,
                    View.TRANSLATION_Y,
                    0.0f,
                    -200.0f,
                    50.0f,
                    0.0f
                )
                translateY.interpolator = LinearInterpolator()
                translateY.duration = 3000L
                translateY.doOnEnd { splashScreenViewProvider.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    splashScreenViewProvider.iconView,
                    View.SCALE_Y,
                    1.0f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { splashScreenViewProvider.remove() }

                val zoomX = ObjectAnimator.ofFloat(
                    splashScreenViewProvider.iconView,
                    View.SCALE_X,
                    1.0f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { splashScreenViewProvider.remove() }

                translateY.start()
                Handler(Looper.getMainLooper()).postDelayed({
                    zoomX.start()
                    zoomY.start()
                }, 2500L)

            }
        }
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(databaseModule)
        }
        enableEdgeToEdge()
        cleanUpOldImages(this)
        isSplashScreenVisible = false
        setContent {
            DiabeetoTheme {
                MainNavigation()
            }
        }
    }
}
