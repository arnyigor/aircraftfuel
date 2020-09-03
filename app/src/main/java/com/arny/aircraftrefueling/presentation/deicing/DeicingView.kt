package com.arny.aircraftrefueling.presentation.deicing

import androidx.annotation.StringRes
import com.arny.aircraftrefueling.data.models.Result
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface DeicingView : MvpView {
    fun showResult(result: Result<Any>)
    fun onVolumeChanged(@StringRes stringRes: Int)
    fun onMassChanged(@StringRes stringRes: Int)
}
