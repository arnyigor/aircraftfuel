package com.arny.aircraftrefueling.data.repository.files

interface IFilesRepository {
    fun saveRefuel(onBoard: Double, require: Double, mRo: Double, volume: Double): Boolean
}
