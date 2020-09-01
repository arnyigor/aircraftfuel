package com.arny.aircraftrefueling.data.repository.units

interface IUnitsRepository {
    fun onVolumeUnitChange(litre: Boolean)
    fun onMassUnitChange(kg: Boolean)
    fun getVolumeUnitName(): String?
    fun getMassUnitName(): String?
    fun getVolumeUnit(): String?
    fun getMassUnit(): String?
    fun getMassUnits(): List<String>
    fun getVolumeUnits(): List<String>
}
