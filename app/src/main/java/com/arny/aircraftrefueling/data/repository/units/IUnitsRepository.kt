package com.arny.aircraftrefueling.data.repository.units

import com.arny.aircraftrefueling.data.models.MeasureUnit
import io.reactivex.Observable

interface IUnitsRepository {
    fun getVolumeUnitObs(): Observable<MeasureUnit>
    fun getMassUnitObs(): Observable<MeasureUnit>
    fun onMassUnitChange(unit: MeasureUnit)
    fun onVolumeUnitChange(unit: MeasureUnit)
    fun getVolumeUnitName(): String?
    fun getMassUnitName(): String?
    fun getVolumeUnit(): String?
    fun getSavedMassUnit(): String?
    fun getUnits(): List<MeasureUnit>
    fun getVolumeUnits(): List<String>
}
