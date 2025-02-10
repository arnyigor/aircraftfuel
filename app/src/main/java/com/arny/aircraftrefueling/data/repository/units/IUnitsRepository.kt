package com.arny.aircraftrefueling.data.repository.units

import com.arny.aircraftrefueling.domain.models.MeasureUnit

interface IUnitsRepository {
    fun onMassUnitChange(unit: MeasureUnit)
    fun onVolumeUnitChange(unit: MeasureUnit)
    fun getVolumeUnitName(): String?
    fun getMassUnitName(): String
    fun getVolumeUnit(): String
    fun getSavedMassUnit(): String?
    fun getUnits(): List<MeasureUnit>
    fun getVolumeUnits(): List<String>
    fun getMassCI(mass: Double, unitName: String?): Double
    fun getVolumeCI(volume: Double, unitName: String?): Double
    fun getMassByUnit(mass: Double, unitName: String?): Double
    fun getVolumeByUnit(volume: Double, volumeDimens: String?): Double
    fun formatTo(value: Double, scale: Int = 0): String
}
