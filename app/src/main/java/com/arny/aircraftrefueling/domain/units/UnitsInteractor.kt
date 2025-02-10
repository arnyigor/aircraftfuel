package com.arny.aircraftrefueling.domain.units

import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.utils.fromSingle
import javax.inject.Inject

class UnitsInteractor @Inject constructor(
        private val unitsRepository: IUnitsRepository
) : IUnitsInteractor {

    override suspend fun loadUnits():List<MeasureUnit> = unitsRepository.getUnits()

    override suspend fun onVolumeUnitChanged(item: MeasureUnit) {
        unitsRepository.onVolumeUnitChange(item)
    }

    override suspend fun onMassUnitChanged(item: MeasureUnit) {
        unitsRepository.onMassUnitChange(item)
    }
}
