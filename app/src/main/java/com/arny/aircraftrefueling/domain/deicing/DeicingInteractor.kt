package com.arny.aircraftrefueling.domain.deicing

class DeicingInteractor {
    fun getPvkMass(total: Double, mRo: Double, percent: Double): Double {
        val d4 = total * (percent / 100.0)
        return total - d4 + d4 * mRo
    }
}
