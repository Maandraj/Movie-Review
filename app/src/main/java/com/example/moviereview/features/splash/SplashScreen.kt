package com.example.moviereview.features.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.moviereview.BuildConfig
import com.example.moviereview.ConfigApp
import com.example.moviereview.MainActivity
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.*


class SplashScreen : AppCompatActivity() {
    private val binding: ActivitySplashScreenBinding by viewBinding(ActivitySplashScreenBinding::bind)
    private val scopeIO: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        waitingGo()
    }


    private suspend fun scheduleSplashScreen() {
        withContext(Dispatchers.Main) {
            binding.tvAppName.startAnimation(AnimationUtils.loadAnimation(this@SplashScreen,
                R.anim.splash_in_name))
            val animation = AnimationUtils.loadAnimation(this@SplashScreen, R.anim.splash_in_logo)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    ConfigApp.firstStart = false
                }

                override fun onAnimationStart(animation: Animation?) {

                }
            })
            binding.ivLogo.startAnimation(animation)
        }

    }


    private fun waitingGo() {
        val splashDuration = getSplashScreenDuration()
        scopeIO.launch {
            if (ConfigApp.firstStart) {
                scheduleSplashScreen()

            }
            delay(splashDuration)
            routeToStart()
        }
    }

    private fun getSplashScreenDuration(): Long {
        return when (ConfigApp.firstStart) {
            true -> {

                MAX_SEC_SPLASH

            }
            false -> {
                MIN_SEC_SPLASH
            }
        }
    }

    private fun routeToStart() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = (
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK

                )
        startActivity(intent)
    }

    override fun onDestroy() {
        scopeIO.cancel()
        super.onDestroy()
    }

    companion object {
        const val MAX_SEC_SPLASH = 2700L
        const val MIN_SEC_SPLASH = 1000L
    }


}