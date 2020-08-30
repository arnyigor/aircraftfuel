package com.arny.aircraftrefueling.domain.refuel

import com.arny.aircraftrefueling.data.models.TankRefuelResult

interface IRefuelInteractor {
    var vUnit: Int

    fun calculate(massReq: Double, mRo: Double, onBoard: Double): TankRefuelResult
}
