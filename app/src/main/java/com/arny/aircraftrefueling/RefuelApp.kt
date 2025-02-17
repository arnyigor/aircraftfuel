package com.arny.aircraftrefueling

import com.arny.aircraftrefueling.di.DaggerAppComponent
import dagger.android.DaggerApplication

class RefuelApp : DaggerApplication(){
    private val applicationInjector = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()
    }
    override fun applicationInjector() = applicationInjector
}