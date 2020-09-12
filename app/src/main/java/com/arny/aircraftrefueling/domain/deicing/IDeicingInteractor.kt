package com.arny.aircraftrefueling.domain.deicing

import com.arny.aircraftrefueling.data.models.MeasureUnit

interface IDeicingInteractor {
    var massUnit: MeasureUnit?
    var volumeUnit: MeasureUnit?
    fun calcMass(totalVolume: Double, mRo: Double, percent: Double): String

    fun saveDeicingData(
            recordData: String?,
            mVolTotal: String,
            mPercPVK: String,
            mRo: String,
            totalMass: String
    ): String
}
