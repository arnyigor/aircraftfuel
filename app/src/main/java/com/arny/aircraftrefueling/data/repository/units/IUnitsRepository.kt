package com.arny.aircraftrefueling.data.repository.units

import com.arny.aircraftrefueling.data.models.MeasureUnit

interface IUnitsRepository {
    fun onMassUnitChange(unit: MeasureUnit)
    fun onVolumeUnitChange(unit: MeasureUnit)
    fun getVolumeUnitName(): String?
    fun getMassUnitName(): String?
    fun getVolumeUnit(): String?
    fun getSavedMassUnit(): String?
    fun getUnits(): List<MeasureUnit>
    fun getVolumeUnits(): List<String>
    fun convertMassFromLb(mass: Double): Double
    fun convertMassToLb(mass: Double): Double
    fun convertVolumeToGal(volume: Double): Double
}
