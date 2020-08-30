package com.arny.aircraftrefueling.presentation.deicing.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arny.aircraftrefueling.RefuelApp
import com.arny.aircraftrefueling.data.repository.FilesRepository
import com.arny.aircraftrefueling.domain.deicing.DeicingInteractor

class DeicingViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeicingViewModel::class.java)) {
            return DeicingViewModel(
                    DeicingInteractor(),
                    FilesRepository(RefuelApp.appContext)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}