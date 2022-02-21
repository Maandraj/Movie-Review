package com.example.moviereview

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
        // Решил отказаться от светлой темы
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

}

object ConfigApp : KotprefModel() {
    var firstStart: Boolean by booleanPref(true)
}

const val HOME_URL = "https://nytimes.com/"