package com.arny.aircraftrefueling.presentation.settings

import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.RefuelApp
import com.arny.aircraftrefueling.data.models.MeasureType
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import com.arny.aircraftrefueling.utils.BaseMvpPresenter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SettingsPresener : BaseMvpPresenter<SettingsView>() {

    @Inject
    lateinit var unitsInteractor: IUnitsInteractor

    init {
        RefuelApp.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        loadUnits()
    }

    private fun loadUnits() {
        unitsInteractor.loadUnits()
                .subscribeFromPresenter({ list ->
                    val massUnits = list.filter { it.type == MeasureType.MASS }
                    val volumeUnits = list.filter { it.type == MeasureType.VOLUME }
                    viewState.setMassUnits(massUnits, massUnits.indexOfFirst { it.selected })
                    viewState.setVolumeUnits(volumeUnits, volumeUnits.indexOfFirst { it.selected })
                }, {
                    viewState.toastError(R.string.load_units_error, it.message)
                })
    }

    fun onVolumeUnitChange(item: MeasureUnit) {
        item.selected = true
        unitsInteractor.onVolumeUnitChanged(item)
    }

    fun onMassUnitChange(item: MeasureUnit) {
        item.selected = true
        unitsInteractor.onMassUnitChanged(item)
    }
}
