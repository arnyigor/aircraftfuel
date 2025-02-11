package com.arny.aircraftrefueling.domain.deicing

import com.arny.aircraftrefueling.data.utils.DataResult
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import kotlinx.coroutines.flow.Flow

interface IDeicingInteractor {
    var massUnit: MeasureUnit?
    var volumeUnit: MeasureUnit?
    suspend fun calcMass(totalVolume: Double, mRo: Double, percent: Double): Flow<DataResult<String>>

    suspend fun saveDeicingData(
            recordData: String?,
            mVolTotal: String,
            mPercPVK: String,
            mRo: String,
            totalMass: String
    ): String
}
