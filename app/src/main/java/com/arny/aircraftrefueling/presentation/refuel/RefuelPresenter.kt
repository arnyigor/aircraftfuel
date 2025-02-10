package com.arny.aircraftrefueling.presentation.refuel

import androidx.annotation.StringRes
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.RefuelApp
import com.arny.aircraftrefueling.domain.constants.Consts
import com.arny.aircraftrefueling.domain.constants.Consts.UNIT_AM_GALL
import com.arny.aircraftrefueling.domain.constants.Consts.UNIT_KG
import com.arny.aircraftrefueling.domain.constants.Consts.UNIT_LB
import com.arny.aircraftrefueling.domain.constants.Consts.UNIT_LITRE
import com.arny.aircraftrefueling.domain.models.MeasureType
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.domain.files.IFilesInteractor
import com.arny.aircraftrefueling.domain.refuel.IRefuelInteractor
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import com.arny.aircraftrefueling.utils.BaseMvpPresenter
import com.arny.aircraftrefueling.utils.fromSingle
import javax.inject.Inject

class RefuelPresenter : BaseMvpPresenter<RefuelView>() {
    private var massUnit: MeasureUnit? = null
    private var volumeUnit: MeasureUnit? = null

    @Inject
    lateinit var interactor: IRefuelInteractor

    @Inject
    lateinit var filesInteractor: IFilesInteractor

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

        // TODO включить потом
/*        fromNullable { filesInteractor.loadSavedRefuelData() }
                .subscribeFromPresenter({ optionalNull ->
                    optionalNull.value?.let {
                        viewState.setEdtRequire(it.mReq)
                        viewState.setEdtRo(it.mRo)
                        viewState.setEdtBoard(it.onBoard)
                    }
                })*/
        checkFileExists()
    }

    private fun checkFileExists() {
        fromSingle { filesInteractor.isDataFileExists() }
                .subscribeFromPresenter({ exists ->
                    viewState.setBtnDelVisible(exists)
                })
    }

    private fun setUnitNames(list: List<MeasureUnit>) {
        val massUnits = list.filter { it.type == MeasureType.MASS }
        val volumeUnits = list.filter { it.type == MeasureType.VOLUME }
        massUnit = massUnits.find { it.selected }
        volumeUnit = volumeUnits.find { it.selected }
        when (massUnit?.name) {
            UNIT_KG -> {
                @StringRes val massKg = R.string.unit_mass_kg
                viewState.setMassUnitName(massKg)
                viewState.setTotalMassUnit(massKg)
                viewState.setOstatMassUnit(massKg)
                viewState.setReqMassUnit(massKg)
            }
            UNIT_LB -> {
                viewState.setMassUnitName(R.string.unit_mass_lb_named)
                @StringRes
                val unitMassLb = R.string.unit_mass_lb
                viewState.setTotalMassUnit(unitMassLb)
                viewState.setOstatMassUnit(unitMassLb)
                viewState.setReqMassUnit(unitMassLb)
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

    fun refuel(density: String, onBoard: String, required: String, type: String) {
        interactor.volumeUnit = volumeUnit
        interactor.massUnit = massUnit
        fromSingle {
            interactor.calculateRefuel(
                required.toDouble(),
                density.toDouble(),
                onBoard.toDouble(),
                type
            )
        }.subscribeFromPresenter({
            viewState.showResult(Result.Success(it))
            viewState.setBtnSaveVisible(true)
            checkFileExists()
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
                        viewState.toastSuccess(R.string.success_file_write, path)
                    } else {
                        viewState.toastError(R.string.error_file_not_write)
                    }
                    checkFileExists()
                }, { e ->
                    e.message?.let {
                        viewState.toastError(it)
                    }
                })
    }

    fun onRemoveFile() {
        fromSingle { filesInteractor.removeFile() }
                .subscribeFromPresenter({ exists ->
                    viewState.setBtnDelVisible(exists)
                    if (exists) {
                        viewState.toastError(R.string.error_file_not_deleted)
                    } else {
                        viewState.toastSuccess(R.string.file_deleted)
                    }
                }, { throwable ->
                    throwable.message?.let {
                        viewState.toastError(it)
                    }
                })
    }
}
