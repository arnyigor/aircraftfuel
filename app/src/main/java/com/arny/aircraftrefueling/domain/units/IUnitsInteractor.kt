package com.arny.aircraftrefueling.domain.units

import com.arny.aircraftrefueling.data.models.MeasureUnit
import io.reactivex.Single

interface IUnitsInteractor {
    fun loadUnits(): Single<List<MeasureUnit>>
    fun onVolumeUnitChanged(item: MeasureUnit)
    fun onMassUnitChanged(item: MeasureUnit)
}
