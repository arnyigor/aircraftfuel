package com.arny.aircraftrefueling.presenter.viewmodel

import com.arny.aircraftrefueling.models.Result

sealed class RerfuelUIState {
    object LoadingState : RerfuelUIState()
    object DefaultState : RerfuelUIState()
    class ResultState(val result: Result<Any>) : RerfuelUIState()
}