package com.arny.aircraftrefueling.presentation.refuel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arny.aircraftrefueling.domain.refuel.TankRefuelInteractor

class RefuelingViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RefuelingViewModel::class.java)) {
            return RefuelingViewModel(TankRefuelInteractor()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}