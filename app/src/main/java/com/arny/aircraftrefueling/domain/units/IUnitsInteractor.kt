package com.arny.aircraftrefueling.domain.units

import com.arny.aircraftrefueling.domain.models.MeasureUnit

interface IUnitsInteractor {
    suspend fun loadUnits(): List<MeasureUnit>
    suspend fun onVolumeUnitChanged(item: MeasureUnit)
    suspend fun onMassUnitChanged(item: MeasureUnit)
}
