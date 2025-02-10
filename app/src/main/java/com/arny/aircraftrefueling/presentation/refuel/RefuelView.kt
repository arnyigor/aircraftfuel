package com.arny.aircraftrefueling.presentation.refuel

import androidx.annotation.StringRes
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface RefuelView : MvpView {
    @OneExecution
    fun toastError(@StringRes errorRes: Int, message: String? = null)

    @OneExecution
    fun toastError(message: String)
    fun setMassUnitName(@StringRes nameRes: Int)
    fun showResult(result: Result<Any>)
    fun setVolumeUnitName(@StringRes nameRes: Int)
    fun setTotalMassUnit(@StringRes unitRes: Int)
    fun setOstatMassUnit(@StringRes unitRes: Int)
    fun setReqMassUnit(@StringRes unitRes: Int)
    fun setOstatVolumeUnit(@StringRes unitRes: Int)
    fun toastSuccess(@StringRes strRes: Int, path: String? = null)
    fun setBtnDelVisible(visible: Boolean)
    fun setBtnSaveVisible(visible: Boolean)
    fun setEdtRequire(mReq: String?)
    fun setEdtRo(mRo: String?)
    fun setEdtBoard(onBoard: String?)
}
