package com.arny.aircraftrefueling.domain.units

import com.arny.aircraftrefueling.data.models.MeasureUnit

interface IUnitsInteractor {
    fun loadUnits(): List<MeasureUnit>
    fun onVolumeUnitChanged(item: MeasureUnit)
    fun onMassUnitChanged(item: MeasureUnit)
}
