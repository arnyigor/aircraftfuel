package com.arny.aircraftrefueling.presentation.deicing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.models.DeicingResult
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.data.repository.FilesRepository
import com.arny.aircraftrefueling.domain.deicing.DeicingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeicingViewModel(private val deicingInteractor: DeicingInteractor, filesRepository: FilesRepository) : ViewModel() {
    private val _uiState = MutableLiveData<DeicingUIState>()
    val uiState: LiveData<DeicingUIState> = _uiState


    fun calculate(onBoard: String, density: String, percent: String, checked: Boolean) {
        viewModelScope.launch {
            try {
                val mRo = density.toDouble()
                if (mRo < 0.0 || mRo > 100.0) {
                    _uiState.value = DeicingUIState.ResultState(
                            Result.ErrorRes(R.string.error_val_proc_pvk)
                    )
                    return@launch
                }
                val refuelResult = withContext(Dispatchers.Default) {
                    deicingInteractor.getPvkMass(onBoard.toDouble(), mRo, percent.toDouble())
                }
                _uiState.value = DeicingUIState.ResultState(Result.Success(DeicingResult(refuelResult)))
            } catch (e: Exception) {
                _uiState.value = DeicingUIState.ResultState(Result.Error(e))
            }
        }
    }
}
