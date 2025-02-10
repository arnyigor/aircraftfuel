package com.arny.aircraftrefueling.presentation.deicing

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.utils.strings.IWrappedString
import com.arny.aircraftrefueling.data.utils.strings.ResourceString
import com.arny.aircraftrefueling.data.utils.strings.SimpleString
import com.arny.aircraftrefueling.domain.constants.Consts
import com.arny.aircraftrefueling.domain.deicing.IDeicingInteractor
import com.arny.aircraftrefueling.domain.files.IFilesInteractor
import com.arny.aircraftrefueling.domain.models.MeasureType
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import com.arny.aircraftrefueling.utils.KeyboardHelper.hideKeyboard
import com.arny.aircraftrefueling.utils.fromSingle
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
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

    private val _deicingUIState = MutableStateFlow<DeicingUIState>(DeicingUIState.IDLE)
    val deicingUIState = _deicingUIState.asStateFlow()

    private val _toastError = MutableSharedFlow<IWrappedString?>()
    val toastError = _toastError.asSharedFlow()

    private val _toastSuccess = MutableSharedFlow<IWrappedString?>()
    val toastSuccess = _toastSuccess.asSharedFlow()

    private val _btnDelVisible = MutableStateFlow(false)
    val btnDelVisible = _btnDelVisible.asStateFlow()

    private val _edtMassUnit = MutableStateFlow<Int?>(null)
    val edtMassUnit = _edtMassUnit.asStateFlow()

    private val _edtVolumeUnit = MutableStateFlow<Int?>(null)
    val edtVolumeUnit = _edtVolumeUnit.asStateFlow()

    private fun loadUnits() {
        viewModelScope.launch {
            flow { emit(unitsInteractor.loadUnits()) }
                .catch { _toastError.emit(ResourceString(R.string.load_units_error, it.message)) }
                .collect(::setUnitNames)
        }
    }

    fun saveData(volume: String, percPvk: String, mRo: String, massTotal: String) {
        viewModelScope.launch {
            flow { emit(interactor.saveDeicingData(null, volume, percPvk, mRo, massTotal)) }
                .catch { _toastError.emit() = SimpleString(it.message) }
                .collect { path ->
                    if (path.isNotBlank()) {
                        _toastSuccess.value = ResourceString(R.string.success_file_write, path)
                    } else {
                        _toastError.value = ResourceString(R.string.error_file_not_write)
                    }
                }
        }
    }

    private fun checkFileExists() {
        viewModelScope.launch {
            flow { emit(filesInteractor.isDataFileExists()) }
                .collect { exists -> _btnDelVisible.value = exists }
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

    fun onCheckPvk(checked: Boolean) {
        viewModelScope.launch {
            _deicingUIState.update {
                DeicingUIState.CheckPVK(checked, if (checked) HALF_PERCENT else FULL_PERCENT)
            }
        }
    }

    fun onLitreCountClick(onBoard: String, density: String, percent: String) {
        when {
            onBoard.isBlank() || onBoard == "0" -> {
                _deicingUIState.update {
                    DeicingUIState.InputError(
                        boardError = ResourceString(R.string.error_deicing_volume_with_unit) to volumeUnitName
                    )
                }
            }

            density.isBlank() || density == "0" -> {
                _deicingUIState.update {
                    DeicingUIState.InputError(
                        densityError = ResourceString(R.string.error_val_density_pvk) to null
                    )
                }
            }

            percent.isBlank() || percent == "0" -> {
                _deicingUIState.update {
                    DeicingUIState.InputError(
                        percentError = ResourceString(R.string.error_val_proc_pvk) to null
                    )
                }
            }
            else->{

            }
        }
        hideKeyboard(requireActivity())
        calculateDeicing(onBoard, density, percent)
    }
}