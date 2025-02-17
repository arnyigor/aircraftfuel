package com.arny.aircraftrefueling.domain.files

import com.arny.aircraftrefueling.data.utils.DataResult
import com.arny.aircraftrefueling.domain.models.RefuelSavedData
import kotlinx.coroutines.flow.Flow

interface IFilesInteractor {
    suspend fun getFilePath(): String?
    suspend fun isDataFileExists(): Boolean
    suspend fun removeFile(): Flow<DataResult<Boolean>>
    suspend fun loadSavedRefuelData(massUnitName: String?): RefuelSavedData?
}
