package com.arny.aircraftrefueling.domain.units

import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.utils.fromSingle
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UnitsInteractor @Inject constructor(
        private val unitsRepository: IUnitsRepository
) : IUnitsInteractor {

    override fun loadUnits(): Single<List<MeasureUnit>> {
        return fromSingle { unitsRepository.getUnits() }
                .subscribeOn(Schedulers.io())
    }

    override fun onVolumeUnitChanged(item: MeasureUnit) {
        unitsRepository.onVolumeUnitChange(item)
    }

    override fun onMassUnitChanged(item: MeasureUnit) {
        unitsRepository.onMassUnitChange(item)
    }
}
