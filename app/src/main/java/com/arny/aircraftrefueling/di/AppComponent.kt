package com.arny.aircraftrefueling.di

import com.arny.aircraftrefueling.presentation.deicing.presenter.DeicingPresenter
import com.arny.aircraftrefueling.presentation.refuel.presenter.RefuelPresenter
import com.arny.aircraftrefueling.presentation.units.UnitsPresener
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(refuelPresenter: RefuelPresenter)
    fun inject(deicingPresenter: DeicingPresenter)
    fun inject(unitsPresener: UnitsPresener)
}