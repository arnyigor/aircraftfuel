package com.arny.aircraftrefueling.domain.refuel

import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.domain.models.TankRefuelResult

interface IRefuelInteractor {
    var massUnit: MeasureUnit?
    var volumeUnit: MeasureUnit?
    fun calculateRefuel(
        mReq: Double,
        mRo: Double,
        mBoard: Double,
        type: String
    ): TankRefuelResult
    suspend fun saveData(
            recordData: String,
            onBoard: String,
            require: String,
            density: String,
            volume: String
    ): String
}
