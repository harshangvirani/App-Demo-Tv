package com.livestreaming.tv

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LiveStreamingTv(): Application() {
    override fun onCreate() {
        super.onCreate()
    }
}