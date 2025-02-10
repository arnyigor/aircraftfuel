package com.arny.aircraftrefueling.domain.files

import com.arny.aircraftrefueling.domain.models.RefuelSavedData

interface IFilesInteractor {
    suspend fun isDataFileExists(): Boolean
    suspend fun removeFile(): Boolean
    suspend fun loadSavedRefuelData(massUnitName: String?): RefuelSavedData?
}
