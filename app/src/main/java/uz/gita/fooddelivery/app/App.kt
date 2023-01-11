package uz.gita.fooddelivery.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import uz.gita.fooddelivery.BuildConfig

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        Timber.d("onCreate")
    }
}