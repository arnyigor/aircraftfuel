package com.arny.aircraftrefueling.di

import com.arny.aircraftrefueling.di.scopes.ActivityScope
import com.arny.aircraftrefueling.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {
    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            HomeFragmentModule::class,
            DeicingFragmentModule::class,
            RefuelFragmentModule::class,
            SettingsFragmentModule::class,
        ]
    )
    abstract fun bindMainActivity(): MainActivity

}
