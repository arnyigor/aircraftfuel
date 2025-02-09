package com.arny.aircraftrefueling.presentation.settings

import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.RefuelApp
import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.data.models.MeasureType
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import com.arny.aircraftrefueling.utils.BaseMvpPresenter
import com.arny.aircraftrefueling.utils.Prefs
import javax.inject.Inject

class SettingsPresener : BaseMvpPresenter<SettingsView>() {

    @Inject
    lateinit var prefs: Prefs

    @Inject
    lateinit var unitsInteractor: IUnitsInteractor

    init {
        RefuelApp.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        loadUnits()
        loadPrefs()
    }

    private fun loadPrefs() {
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

    fun onSaveLastRefuelDataChanged(checked: Boolean) {
        prefs.put(Consts.PREF_SAVE_REFUEL_LAST_DATA, checked)
        if (!checked) {
            prefs.remove(Consts.PREF_REFUEL_LAST_DATA_REQUIRE)
            prefs.remove(Consts.PREF_REFUEL_LAST_DATA_BOARD)
            prefs.remove(Consts.PREF_REFUEL_LAST_DATA_RO)
        }
    }
}
