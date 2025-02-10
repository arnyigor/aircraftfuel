package com.arny.aircraftrefueling.di

import android.content.Context
import com.arny.aircraftrefueling.RefuelApp
import dagger.Binds
import dagger.Module

@Module
internal abstract class AppModule {
    @Binds
    abstract fun provideContext(application: RefuelApp): Context
}