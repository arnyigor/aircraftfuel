package com.arny.aircraftrefueling.data.repository.units

import com.arny.aircraftrefueling.data.models.MeasureUnit

interface IUnitsRepository {
    fun onVolumeUnitChange(litre: Boolean)
    fun onMassUnitChange(kg: Boolean)
    fun getVolumeUnitName(): String?
    fun getMassUnitName(): String?
    fun getVolumeUnit(): String?
    fun getSavedMassUnit(): String?
    fun getUnits(): List<MeasureUnit>
    fun getVolumeUnits(): List<String>
}
