package com.arny.aircraftrefueling.data.repository.files

interface IFilesRepository {
    suspend fun saveRefuelData(
        recordData: String?,
        onBoard: String,
        require: String,
        mRo: String,
        volume: String
    ): String

    suspend fun isDataFileExists(): Boolean
    suspend fun removeFile(): Boolean
    suspend fun saveDeicingData(
        recordData: String?,
        mVolTotal: String,
        mPercPVK: String,
        mRo: String,
        totalMass: String
    ): String
}
