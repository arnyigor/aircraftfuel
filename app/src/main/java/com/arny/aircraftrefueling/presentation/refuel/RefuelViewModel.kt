package com.arny.aircraftrefueling.presentation.refuel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.utils.DataResult
import com.arny.aircraftrefueling.data.utils.strings.IWrappedString
import com.arny.aircraftrefueling.data.utils.strings.ResourceString
import com.arny.aircraftrefueling.data.utils.strings.SimpleString
import com.arny.aircraftrefueling.domain.constants.Consts
import com.arny.aircraftrefueling.domain.files.IFilesInteractor
import com.arny.aircraftrefueling.domain.models.DataThrowable
import com.arny.aircraftrefueling.domain.models.MeasureType
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.domain.refuel.IRefuelInteractor
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
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

    @StringRes
    private var massUnitName: Int = R.string.unit_mass_kg

    @StringRes
    private var volumeUnitName: Int = R.string.unit_volume_named
    private var volumeUnit: MeasureUnit? = null

    private val _refuelUIState = MutableStateFlow<RefuelUIState>(RefuelUIState.IDLE)
    val refuelUIState = _refuelUIState.asStateFlow()

    private val _toastError = MutableSharedFlow<IWrappedString?>()
    val toastError = _toastError.asSharedFlow()

    private val _hideKeyboard = MutableSharedFlow<Unit>()
    val hideKeyboard = _hideKeyboard.asSharedFlow()

    private val _toastSuccess = MutableSharedFlow<IWrappedString?>()
    val toastSuccess = _toastSuccess.asSharedFlow()

    private val _btnDelVisible = MutableStateFlow(false)
    val btnDelVisible = _btnDelVisible.asStateFlow()

    private val _btnSaveVisible = MutableStateFlow(false)
    val btnSaveVisible = _btnSaveVisible.asStateFlow()

    private val _edtMassUnit = MutableStateFlow<Int?>(null)
    val edtMassUnit = _edtMassUnit.asStateFlow()

    private val _edtVolumeUnit = MutableStateFlow<Int?>(null)
    val edtVolumeUnit = _edtVolumeUnit.asStateFlow()

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
            Consts.UNIT_KG -> {
                massUnitName = R.string.unit_mass_kg
                _edtMassUnit.value = R.string.unit_mass_kg
            }

            Consts.UNIT_LB -> {
                massUnitName = R.string.unit_mass_lb_named
                _edtMassUnit.value = R.string.unit_mass_lb
            }
        }
        when (volumeUnit?.name) {
            Consts.UNIT_LITRE -> {
                volumeUnitName = R.string.unit_volume_named
                _edtVolumeUnit.value = R.string.unit_litre
            }

            Consts.UNIT_AM_GALL -> {
                volumeUnitName = R.string.unit_am_gallons_named
                _edtVolumeUnit.value = R.string.unit_am_gallons
            }
        }
    }

    fun refuel(density: String, onBoard: String, required: String, type: String) {
        viewModelScope.launch {
            interactor.volumeUnit = volumeUnit
            interactor.massUnit = massUnit
            interactor.calculateRefuel(
                mReq = required.toDouble(),
                mRo = density.toDouble(),
                mBoard = onBoard.toDouble(),
                type = type
            )
                .catch { onError(it) }
                .collect { result ->
                    when (result) {
                        is DataResult.Error -> onError(result.throwable)
                        is DataResult.Success -> {
                            _refuelUIState.update {
                                RefuelUIState.Result(true, result.result)
                            }
                            _btnSaveVisible.value = true
                            checkFileExists()
                        }
                    }
                }
        }
    }

    private fun onError(throwable: Throwable) {
        when {
            throwable.message == Consts.ERROR_TOTAL_LESS -> {
                _refuelUIState.update {
                    RefuelUIState.Result(false, error = ResourceString(R.string.error_val_on_board))
                }
            }

            throwable is DataThrowable -> RefuelUIState.Result(
                false,
                error = throwable.wrappedString
            )
        }
    }

    fun calculateFuelCapacity(onBoard: String, required: String, density: String, type: String) {
        viewModelScope.launch {
            when {
                onBoard.isBlank() || onBoard == "0" -> {
                    _refuelUIState.update {
                        RefuelUIState.InputError(
                            boardError = R.string.error_val_on_board_with_unit to massUnitName
                        )
                    }
                }

                required.isBlank() || required == "0" -> {
                    _refuelUIState.update {
                        RefuelUIState.InputError(
                            requiredError = R.string.error_val_req to massUnitName
                        )
                    }
                }

                density.isBlank() || density == "0" -> {
                    _refuelUIState.update {
                        RefuelUIState.InputError(
                            densityError = R.string.error_val_density to null
                        )
                    }
                }
            }
            _hideKeyboard.emit(Unit)
            refuel(density, onBoard, required, type)
        }
    }

    fun saveData(
        recordData: String,
        onBoard: String,
        require: String,
        density: String,
        volume: String,
    ) {
        viewModelScope.launch {
            flow { emit(interactor.saveData(recordData, onBoard, require, density, volume)) }
                .catch { _toastError.emit(SimpleString(it.message)) }
                .collect { path ->
                    if (path.isNotBlank()) {
                        _toastSuccess.emit(ResourceString(R.string.success_file_write, path))
                    } else {
                        _toastError.emit(ResourceString(R.string.error_file_not_write))
                    }
                }
        }
    }

    fun onRemoveFile() {
        viewModelScope.launch {
            filesInteractor.removeFile()
                .collect { result ->
                    when (result) {
                        is DataResult.Error -> {
                            _toastError.emit(SimpleString(result.throwable.message))
                        }

                        is DataResult.Success -> {
                            val exists = result.result
                            _btnDelVisible.emit(exists)
                            if (exists) {
                                _toastError.emit(ResourceString(R.string.error_file_not_deleted))
                            } else {
                                _toastSuccess.emit(ResourceString(R.string.file_deleted))
                            }
                        }
                    }
                }

        }
    }
}