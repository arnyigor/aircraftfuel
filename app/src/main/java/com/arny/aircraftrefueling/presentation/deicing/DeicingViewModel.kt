package com.arny.aircraftrefueling.presentation.deicing

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.domain.deicing.IDeicingInteractor
import com.arny.aircraftrefueling.domain.files.IFilesInteractor
import com.arny.aircraftrefueling.domain.models.MeasureType
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import com.arny.aircraftrefueling.utils.fromSingle
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DeicingViewModel @AssistedInject constructor(
    private val interactor: IDeicingInteractor,
    private val filesInteractor: IFilesInteractor,
    private val unitsInteractor: IUnitsInteractor,
) : ViewModel() {

    private companion object {
        const val FULL_PERCENT = "100"
        const val HALF_PERCENT = "50"
    }

    private var massUnit: MeasureUnit? = null
    private var volumeUnit: MeasureUnit? = null

    fun onVolumeUnitChange(item: MeasureUnit?) {
        TODO("Not yet implemented")
    }

    fun onMassUnitChange(item: MeasureUnit?) {
        TODO("Not yet implemented")
    }

    fun saveData(volume: String, percPvk: String, mRo: String, massTotal: String) {
        viewModelScope.launch {
            flow { emit(interactor.saveDeicingData(null, volume, percPvk, mRo, massTotal)) }
                .collect { path ->
                    if (path.isNotBlank()) {
                        viewState.toastSuccess(R.string.success_file_write, path)
                    } else {
                        viewState.toastError(R.string.error_file_not_write)
                    }
                }
            fromSingle { interactor.saveDeicingData(null, volume, percPvk, mRo, massTotal) }
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
            Consts.UNIT_KG -> {
                @StringRes
                val massKg = R.string.unit_mass_kg
                viewState.setMassUnitName(massKg)
                viewState.setEdtMassUnit(massKg)
            }

            Consts.UNIT_LB -> {
                @StringRes
                val unitMassLb = R.string.unit_mass_lb
                viewState.setMassUnitName(R.string.unit_mass_lb_named)
                viewState.setEdtMassUnit(unitMassLb)
            }
        }

        when (volumeUnit?.name) {
            Consts.UNIT_LITRE -> {
                viewState.setVolumeUnitName(R.string.unit_volume_named)
                viewState.setEdtVolumeUnit(R.string.unit_litre)
            }

            Consts.UNIT_AM_GALL -> {
                viewState.setVolumeUnitName(R.string.unit_am_gallons_named)
                viewState.setEdtVolumeUnit(R.string.unit_am_gallons)
            }
        }
    }

    fun calculateDeicing(onBoard: String, density: String, percent: String) {
        interactor.volumeUnit = volumeUnit
        interactor.massUnit = massUnit
        fromSingle { getResult(onBoard, density, percent) }
            .subscribeFromPresenter({
                viewState.showResult(it)
                viewState.setBtnSaveVisible(true)
                checkFileExists()
            }, {
                viewState.showResult(Result.Error(it))
            })
    }

    private fun getResult(onBoard: String, densityStr: String, percent: String): Result<String> {
        val density = densityStr.toDouble()
        if (density !in 0.0..100.0) {
            return Result.ErrorRes(R.string.error_val_proc_pvk)
        }
        return Result.Success(interactor.calcMass(onBoard.toDouble(), density, percent.toDouble()))
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