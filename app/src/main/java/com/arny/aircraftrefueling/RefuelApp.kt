package com.arny.aircraftrefueling

import com.arny.aircraftrefueling.di.DaggerAppComponent
import dagger.android.DaggerApplication
import timber.log.Timber

class RefuelApp : DaggerApplication(){
    private val applicationInjector = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
    override fun applicationInjector() = applicationInjector
}