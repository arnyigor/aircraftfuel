package com.arny.aircraftrefueling.presentation.refuel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.utils.strings.IWrappedString
import com.arny.aircraftrefueling.data.utils.strings.ResourceString
import com.arny.aircraftrefueling.domain.constants.Consts
import com.arny.aircraftrefueling.domain.constants.Consts.UNIT_AM_GALL
import com.arny.aircraftrefueling.domain.constants.Consts.UNIT_KG
import com.arny.aircraftrefueling.domain.constants.Consts.UNIT_LB
import com.arny.aircraftrefueling.domain.constants.Consts.UNIT_LITRE
import com.arny.aircraftrefueling.domain.files.IFilesInteractor
import com.arny.aircraftrefueling.domain.models.MeasureType
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.domain.refuel.IRefuelInteractor
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import com.arny.aircraftrefueling.utils.fromSingle
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RefuelViewModel @AssistedInject constructor(
    private val interactor: IRefuelInteractor,
    private val filesInteractor: IFilesInteractor,
    private val unitsInteractor: IUnitsInteractor,
) : ViewModel() {

    init {
        loadUnits()
        checkFileExists()
    }

    private var massUnit: MeasureUnit? = null
    private var volumeUnit: MeasureUnit? = null

    private val _toastError = MutableSharedFlow<IWrappedString?>()
    val error = _toastError.asSharedFlow()

    private val _btnDelVisible = MutableStateFlow(false)
    val btnDelVisible = _btnDelVisible.asStateFlow()

    private fun loadUnits() {
        viewModelScope.launch {
            flow { emit(unitsInteractor.loadUnits()) }
                .catch { _toastError.emit(ResourceString(R.string.load_units_error, it.message)) }
                .collect { list ->
                    setUnitNames(list)
                }
        }
    }

    private fun checkFileExists() {
        viewModelScope.launch {
            flow { emit(filesInteractor.isDataFileExists()) }
                .collect { exists ->
                    _btnDelVisible.value = exists
                }
        }
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