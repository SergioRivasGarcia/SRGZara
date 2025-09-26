package com.srg.zara.application

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SRGZaraApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

}