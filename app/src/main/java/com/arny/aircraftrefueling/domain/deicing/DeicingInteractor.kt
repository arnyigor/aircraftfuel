package com.arny.aircraftrefueling.domain.deicing

import com.arny.aircraftrefueling.data.repository.files.IFilesRepository
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import javax.inject.Inject

class DeicingInteractor @Inject constructor(
        private val filesRepository: IFilesRepository,
        private val unitsRepository: IUnitsRepository
) : IDeicingInteractor {
    override fun calcMass(total: Double, mRo: Double, percent: Double): Double {
        val d4 = total * (percent / 100.0)
        return total - d4 + d4 * mRo
    }

    override fun onVolumeUnitChange(checked: Boolean) {
        unitsRepository.onVolumeUnitChange(!checked)
    }

    override fun onMassUnitChange(checked: Boolean) {
        unitsRepository.onMassUnitChange(!checked)
    }
}
