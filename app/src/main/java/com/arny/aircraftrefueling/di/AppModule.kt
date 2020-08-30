package com.arny.aircraftrefueling.di

import android.app.Application
import android.content.Context
import com.arny.aircraftrefueling.data.di.DataModule
import com.arny.aircraftrefueling.domain.di.DomainModule
import com.arny.aircraftrefueling.utils.Prefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    DataModule::class,
    DomainModule::class
])
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun providePrefs(): Prefs  = Prefs.getInstance(application)
}
