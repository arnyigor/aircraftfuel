package com.arny.aircraftrefueling.data.repository.files

interface IFilesRepository {
    fun saveRefuel(recordData: String?, onBoard: String, require: String, mRo: String, volume: String): String
}
