package com.arny.aircraftrefueling.data.repository.files

interface IFilesRepository {
    fun saveRefuelData(recordData: String?, onBoard: String, require: String, mRo: String, volume: String): String
    fun isDataFileExists(): Boolean
    fun removeFile(): Boolean
    fun saveDeicingData(
            recordData: String?,
            mVolTotal: String,
            mPercPVK: String,
            mRo: String,
            totalMass: String
    ): String
}
