package com.arny.aircraftrefueling.presentation.units

import androidx.annotation.StringRes
import com.arny.aircraftrefueling.data.models.MeasureUnit
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface UnitsView : MvpView {
    fun toastError(@StringRes errorRes: Int, message: String?)
    fun setMassUnits(massUnits: List<MeasureUnit>, selectedIndex: Int)
    fun setVolumeUnits(volumeUnits: List<MeasureUnit>, selectedIndex: Int)
}
