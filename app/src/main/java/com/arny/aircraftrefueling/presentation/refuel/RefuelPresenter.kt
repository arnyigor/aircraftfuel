package com.arny.aircraftrefueling.presentation.refuel

import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.RefuelApp
import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.constants.Consts.UNIT_AM_GALL
import com.arny.aircraftrefueling.constants.Consts.UNIT_KG
import com.arny.aircraftrefueling.constants.Consts.UNIT_LB
import com.arny.aircraftrefueling.constants.Consts.UNIT_LITRE
import com.arny.aircraftrefueling.data.models.MeasureType
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.domain.refuel.IRefuelInteractor
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import com.arny.aircraftrefueling.utils.BaseMvpPresenter
import com.arny.aircraftrefueling.utils.fromSingle
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class RefuelPresenter : BaseMvpPresenter<RefuelView>() {
    private var massUnit: MeasureUnit? = null
    private var volumeUnit: MeasureUnit? = null

    @Inject
    lateinit var interactor: IRefuelInteractor

    @Inject
    lateinit var unitsInteractor: IUnitsInteractor

    init {
        RefuelApp.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        unitsInteractor.loadUnits()
                .subscribeFromPresenter({ list ->
                    setUnitNames(list)
                }, {
                    viewState.toastError(R.string.load_units_error, it.message)
                })
    }

    private fun setUnitNames(list: List<MeasureUnit>) {
        val massUnits = list.filter { it.type == MeasureType.MASS }
        val volumeUnits = list.filter { it.type == MeasureType.VOLUME }
        massUnit = massUnits.find { it.selected }
        volumeUnit = volumeUnits.find { it.selected }
        when (massUnit?.name) {
            UNIT_KG -> {
                viewState.setMassUnitName(R.string.unit_mass_kg)
                viewState.setTotalMassUnit(R.string.unit_mass_kg)
                viewState.setOstatMassUnit(R.string.unit_mass_kg)
                viewState.setReqMassUnit(R.string.unit_mass_kg)
            }
            UNIT_LB -> {
                viewState.setMassUnitName(R.string.unit_mass_lb_named)
                viewState.setTotalMassUnit(R.string.unit_mass_lb)
                viewState.setOstatMassUnit(R.string.unit_mass_lb)
                viewState.setReqMassUnit(R.string.unit_mass_lb)
            }
        }

        when (volumeUnit?.name) {
            UNIT_LITRE -> {
                viewState.setVolumeUnitName(R.string.unit_volume_named)
                viewState.setOstatVolumeUnit(R.string.unit_litre)
            }
            UNIT_AM_GALL -> {
                viewState.setVolumeUnitName(R.string.unit_am_gallons_named)
                viewState.setOstatVolumeUnit(R.string.unit_am_gallons)
            }
        }
    }

    fun refuel(density: String, onBoard: String, required: String) {
        interactor.volumeUnit = volumeUnit
        interactor.massUnit = massUnit
        fromSingle {
            interactor.calculate(
                    required.toDouble(),
                    density.toDouble(),
                    onBoard.toDouble()
            )
        }.subscribeFromPresenter({
            viewState.showResult(Result.Success(it))
            viewState.setBtnSaveVisible(true)
        }, { e ->
            if (e.message == Consts.ERROR_TOTAL_LESS) {
                viewState.showResult(Result.ErrorRes(R.string.error_val_on_board))
            } else {
                viewState.showResult(Result.Error(e))
            }
        })
    }

    fun saveData(
            recordData: String,
            onBoard: String,
            require: String,
            density: String,
            volume: String,
    ) {
        fromSingle { interactor.saveData(recordData, onBoard, require, density, volume) }
                .subscribeFromPresenter({ path ->
                    if (path.isNotBlank()) {
                        viewState.setSaveResult(R.string.success_file_write, path)
                    } else {
                        viewState.toastError(R.string.error_file_not_write)
                    }
                }, { e ->
                    e.printStackTrace()
                    e.message?.let {
                        viewState.toastError(it)
                    }
                })
    }
}
