package com.arny.aircraftrefueling.presentation.deicing

import androidx.annotation.StringRes
import com.arny.aircraftrefueling.data.models.Result
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import moxy.viewstate.strategy.alias.OneExecution

@StateStrategyType(AddToEndSingleStrategy::class)
interface DeicingView : MvpView {
    @OneExecution
    fun toastError(@StringRes errorRes: Int, message: String? = null)

    @OneExecution
    fun toastError(message: String)

    fun showResult(result: Result<Any>)
    fun setMassUnitName(@StringRes nameRes: Int)
    fun setVolumeUnitName(@StringRes nameRes: Int)
    fun setEdtMassUnit(@StringRes unitRes: Int)
    fun setEdtVolumeUnit(@StringRes unitRes: Int)
    fun toastSuccess(@StringRes strRes: Int, path: String? = null)
    fun setBtnDelVisible(visible: Boolean)
    fun setBtnSaveVisible(visible: Boolean)
}
