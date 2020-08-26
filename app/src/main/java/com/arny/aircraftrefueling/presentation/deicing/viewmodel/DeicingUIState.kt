package com.arny.aircraftrefueling.presentation.deicing.viewmodel

import com.arny.aircraftrefueling.data.models.Result

sealed class DeicingUIState {
    object LoadingState : DeicingUIState()
    object DefaultState : DeicingUIState()
    class ResultState(val result: Result<Any>) : DeicingUIState()
}
