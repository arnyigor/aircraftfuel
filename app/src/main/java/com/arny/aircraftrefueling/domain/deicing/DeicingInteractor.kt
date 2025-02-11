package com.arny.aircraftrefueling.domain.deicing

import com.arny.aircraftrefueling.data.repository.files.IFilesRepository
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.data.utils.DataResult
import com.arny.aircraftrefueling.data.utils.doAsync
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeicingInteractor @Inject constructor(
    private val filesRepository: IFilesRepository,
    private val unitsRepository: IUnitsRepository,
) : IDeicingInteractor {
    override var massUnit: MeasureUnit? = null
    override var volumeUnit: MeasureUnit? = null

    override suspend fun calcMass(
        totalVolume: Double,
        mRo: Double,
        percent: Double
    ): Flow<DataResult<String>> = doAsync {
        val mTotal: Double = unitsRepository.getVolumeCI(totalVolume, volumeUnit?.name)
        val percentedVolume = mTotal * (percent / 100.0)
        val mass = (mTotal - percentedVolume) + (percentedVolume * mRo)
        unitsRepository.formatTo(unitsRepository.getMassByUnit(mass, massUnit?.name))
    }

    override suspend fun saveDeicingData(
        recordData: String?,
        mVolTotal: String,
        mPercPVK: String,
        mRo: String,
        totalMass: String
    ): String = filesRepository.saveDeicingData(recordData, mVolTotal, mPercPVK, mRo, totalMass)
}
