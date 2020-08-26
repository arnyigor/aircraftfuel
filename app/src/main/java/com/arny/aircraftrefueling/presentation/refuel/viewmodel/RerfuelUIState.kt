package com.arny.aircraftrefueling.presentation.refuel.viewmodel

import com.arny.aircraftrefueling.data.models.Result

sealed class RerfuelUIState {
    object LoadingState : RerfuelUIState()
    object DefaultState : RerfuelUIState()
    class ResultState(val result: Result<Any>) : RerfuelUIState()
}