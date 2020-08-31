package com.arny.aircraftrefueling.domain.di

import com.arny.aircraftrefueling.domain.deicing.DeicingInteractor
import com.arny.aircraftrefueling.domain.deicing.IDeicingInteractor
import com.arny.aircraftrefueling.domain.refuel.IRefuelInteractor
import com.arny.aircraftrefueling.domain.refuel.RefuelInteractor
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DomainModule {
    @Binds
    @Singleton
    fun bindsInteractor(interactor: RefuelInteractor): IRefuelInteractor

    @Binds
    @Singleton
    fun bindsDeicingInteractor(interactor: DeicingInteractor): IDeicingInteractor

}