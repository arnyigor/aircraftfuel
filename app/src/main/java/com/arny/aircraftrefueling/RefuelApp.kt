package com.arny.aircraftrefueling

import android.app.Application
import com.arny.aircraftrefueling.di.AppComponent
import com.arny.aircraftrefueling.di.AppModule
import com.arny.aircraftrefueling.di.DaggerAppComponent

class RefuelApp : Application() {
    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}