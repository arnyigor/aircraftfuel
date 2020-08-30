package com.arny.aircraftrefueling.presentation.refuel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.constants.Consts.ERROR_TOTAL_LESS
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.data.repository.FilesRepository
import com.arny.aircraftrefueling.domain.refuel.TankRefuelInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RefuelingViewModel(
        private val tankRefuelInteractor: TankRefuelInteractor,
        private val filesRepository: FilesRepository
) : ViewModel() {
    private val _uiState = MutableLiveData<RerfuelUIState>()
    val uiState: LiveData<RerfuelUIState> = _uiState

    fun refuel(density: String, onBoard: String, required: String, volumeUnitType: Int) {
        viewModelScope.launch {
            try {
                val refuelResult = withContext(Dispatchers.Default) {
                    tankRefuelInteractor.vUnit = volumeUnitType
                    tankRefuelInteractor.calculate(required.toDouble(), density.toDouble(), onBoard.toDouble())
                }
                _uiState.value = RerfuelUIState.ResultState(Result.Success(refuelResult))
            } catch (e: Exception) {
                if (e.message == ERROR_TOTAL_LESS) {
                    _uiState.value = RerfuelUIState.ResultState(Result.ErrorRes(R.string.error_val_on_board))
                } else {
                    _uiState.value = RerfuelUIState.ResultState(Result.Error(e))
                }
            }
        }
    }
}
