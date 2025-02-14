package com.arny.aircraftrefueling.domain.refuel

import com.arny.aircraftrefueling.data.utils.DataResult
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.domain.models.TankRefuelResult
import kotlinx.coroutines.flow.Flow

interface IRefuelInteractor {
    var massUnit: MeasureUnit?
    var volumeUnit: MeasureUnit?
    suspend fun calculateRefuel(
        mReq: Double,
        mRo: Double,
        mBoard: Double,
        type: String
    ): Flow<DataResult<TankRefuelResult>>
    suspend fun saveData(
            recordData: String,
            onBoard: String,
            require: String,
            density: String,
            volume: String
    ): String
}
