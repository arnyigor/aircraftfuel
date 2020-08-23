package com.arny.aircraftrefueling.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RefuelingViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RefuelingViewModel::class.java)) {
            return RefuelingViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}