package com.arny.aircraftrefueling.di

import com.arny.aircraftrefueling.di.scopes.FragmentScope
import com.arny.aircraftrefueling.presentation.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface HomeFragmentModule {
    @FragmentScope
    @ContributesAndroidInjector()
    fun contributeFragmentInjector(): HomeFragment
}
