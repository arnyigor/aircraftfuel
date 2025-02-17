package com.arny.aircraftrefueling.di

import com.arny.aircraftrefueling.di.scopes.FragmentScope
import com.arny.aircraftrefueling.presentation.deicing.DeicingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DeicingFragmentModule {
    @FragmentScope
    @ContributesAndroidInjector()
    fun contributeFragmentInjector(): DeicingFragment
}
