package com.arny.aircraftrefueling.domain.units

import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.utils.fromSingle
import javax.inject.Inject

class UnitsInteractor @Inject constructor(
        private val unitsRepository: IUnitsRepository
) : IUnitsInteractor {

    override fun loadUnits():List<MeasureUnit> {
        return fromSingle { unitsRepository.getUnits() }
    }

    override fun onVolumeUnitChanged(item: MeasureUnit) {
        unitsRepository.onVolumeUnitChange(item)
    }

    override fun onMassUnitChanged(item: MeasureUnit) {
        unitsRepository.onMassUnitChange(item)
    }
}
