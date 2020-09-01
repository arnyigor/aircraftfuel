package com.arny.aircraftrefueling.presentation.units

import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import com.arny.aircraftrefueling.utils.BaseMvpPresenter
import moxy.InjectViewState

@InjectViewState
class UnitsPresener constructor(
        private val unitsInteractor: IUnitsInteractor
) : BaseMvpPresenter<UnitsView>() {
    override fun onFirstViewAttach() {
        loadUnits()
    }

    private fun loadUnits() {
        unitsInteractor.loadMassUnits()
                .subscribeFromPresenter({
                    it.
                },{

                })
    }
}
