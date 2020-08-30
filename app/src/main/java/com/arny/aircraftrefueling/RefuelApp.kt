package com.arny.aircraftrefueling

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class RefuelApp : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        appContext = applicationContext;
    }
}