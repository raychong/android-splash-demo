package com.pentagon.splashscreendemo.splash

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel

class SplashViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        const val WORK_DURATION = 5000L
    }
    private val initTime = SystemClock.uptimeMillis()
    fun isDataReady() = SystemClock.uptimeMillis() - initTime > WORK_DURATION
}