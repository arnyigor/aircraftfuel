package com.arny.aircraftrefueling.presentation.refuel

import androidx.annotation.StringRes
import com.arny.aircraftrefueling.data.models.Result
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface RefuelView : MvpView {
    @OneExecution
    fun toastError(@StringRes errorRes: Int, message: String?)
    fun setMassUnitName(@StringRes nameRes: Int)
    fun showResult(result: Result<Any>)
    fun setVolumeUnitName(@StringRes nameRes: Int)
    fun setTotalMassUnit(@StringRes unitRes: Int)
    fun setOstatMassUnit(@StringRes unitRes: Int)
    fun setReqMassUnit(@StringRes unitRes: Int)
    fun setOstatVolumeUnit(@StringRes unitRes: Int)
}
