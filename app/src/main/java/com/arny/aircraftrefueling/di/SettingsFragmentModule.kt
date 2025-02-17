package com.arny.aircraftrefueling.di

import com.arny.aircraftrefueling.di.scopes.FragmentScope
import com.arny.aircraftrefueling.presentation.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface SettingsFragmentModule {
    @FragmentScope
    @ContributesAndroidInjector()
    fun contributeFragmentInjector(): SettingsFragment
}
