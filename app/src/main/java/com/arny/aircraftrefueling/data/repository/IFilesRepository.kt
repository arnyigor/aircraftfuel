package com.arny.aircraftrefueling.data.repository

interface IFilesRepository {
    fun saveRefuel(onBoard: Double, require: Double, mRo: Double, volume: Double): Boolean
}
