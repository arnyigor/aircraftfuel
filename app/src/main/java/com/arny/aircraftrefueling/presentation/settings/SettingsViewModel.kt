package com.arny.aircraftrefueling.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.repository.Prefs
import com.arny.aircraftrefueling.data.utils.strings.IWrappedString
import com.arny.aircraftrefueling.data.utils.strings.ResourceString
import com.arny.aircraftrefueling.domain.constants.Consts
import com.arny.aircraftrefueling.domain.models.MeasureType
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SettingsViewModel @AssistedInject constructor(
    private val prefs: Prefs,
    private val unitsInteractor: IUnitsInteractor
) : ViewModel() {

    init {
        loadUnits()
    }

    private val _toastError = MutableSharedFlow<IWrappedString?>()
    val toastError = _toastError.asSharedFlow()

    private val _hideKeyboard = MutableSharedFlow<Unit>()
    val hideKeyboard = _hideKeyboard.asSharedFlow()

    private val _massUnits = MutableStateFlow<List<MeasureUnit>>(emptyList())
    val massUnits = _massUnits.asStateFlow()

    private val _volumeUnits = MutableStateFlow<List<MeasureUnit>>(emptyList())
    val volumeUnits = _volumeUnits.asStateFlow()

    private fun loadUnits() {
        viewModelScope.launch {
            flow { emit(unitsInteractor.loadUnits()) }
                .catch { _toastError.emit(ResourceString(R.string.load_units_error, it.message)) }
                .collect { list ->
                    val massUnits = list.filter { it.type == MeasureType.MASS }
                    val volumeUnits = list.filter { it.type == MeasureType.VOLUME }
                    _massUnits.value = massUnits
                    _volumeUnits.value = volumeUnits
                }
        }
    }

    fun onVolumeUnitChange(item: MeasureUnit) {
        viewModelScope.launch {
            item.selected = true
            unitsInteractor.onVolumeUnitChanged(item)
        }
    }

    fun onMassUnitChange(item: MeasureUnit) {
       viewModelScope.launch {
           item.selected = true
           unitsInteractor.onMassUnitChanged(item)
       }
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