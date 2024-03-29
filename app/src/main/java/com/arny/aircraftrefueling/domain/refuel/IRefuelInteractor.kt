package com.arny.aircraftrefueling.domain.refuel

import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.data.models.TankRefuelResult

interface IRefuelInteractor {
    var massUnit: MeasureUnit?
    var volumeUnit: MeasureUnit?
    fun calculateRefuel(
        mReq: Double,
        mRo: Double,
        mBoard: Double,
        type: String
    ): TankRefuelResult
    fun saveData(
            recordData: String,
            onBoard: String,
            require: String,
            density: String,
            volume: String
    ): String
}
