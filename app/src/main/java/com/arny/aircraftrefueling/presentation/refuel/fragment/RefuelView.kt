package com.arny.aircraftrefueling.presentation.refuel.fragment

import com.arny.aircraftrefueling.data.models.Result
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface RefuelView : MvpView {
    @AddToEndSingle
    fun showResult(result: Result<Any>)
}
