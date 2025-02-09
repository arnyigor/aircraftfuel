package com.arny.aircraftrefueling.presentation.deicing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.utils.fromSingle
import kotlinx.coroutines.launch

class DeicingViewModel : ViewModel() {
    fun onVolumeUnitChange(item: MeasureUnit?) {
        TODO("Not yet implemented")
    }

    fun onMassUnitChange(item: MeasureUnit?) {
        TODO("Not yet implemented")
    }

    fun saveData(volume: String, percPvk: String, mRo: String, massTotal: String) {
        viewModelScope.launch {
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
}