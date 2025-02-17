package com.arny.aircraftrefueling.di

import com.arny.aircraftrefueling.di.scopes.FragmentScope
import com.arny.aircraftrefueling.presentation.refuel.RefuelFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface RefuelFragmentModule {
    @FragmentScope
    @ContributesAndroidInjector()
    fun contributeFragmentInjector(): RefuelFragment
}
