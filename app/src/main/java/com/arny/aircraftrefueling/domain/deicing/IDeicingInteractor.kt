package com.arny.aircraftrefueling.domain.deicing

interface IDeicingInteractor {
    fun calcMass(total: Double, mRo: Double, percent: Double): Double
}
