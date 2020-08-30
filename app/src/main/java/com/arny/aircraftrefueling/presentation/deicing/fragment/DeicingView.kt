package com.arny.aircraftrefueling.presentation.deicing.fragment

import com.arny.aircraftrefueling.data.models.Result
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface DeicingView : MvpView {
    fun showResult(result: Result<Any>)
}
