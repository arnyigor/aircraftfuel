package com.arny.aircraftrefueling.domain.refuel

import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.data.models.TankRefuelResult

interface IRefuelInteractor {
    var massUnit: MeasureUnit?
    var volumeUnit: MeasureUnit?
    fun calculate(massReq: Double, mRo: Double, onBoard: Double): TankRefuelResult
}
