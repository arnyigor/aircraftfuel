package com.arny.aircraftrefueling.domain.files

import com.arny.aircraftrefueling.domain.models.RefuelSavedData

interface IFilesInteractor {
    fun isDataFileExists(): Boolean
    fun removeFile(): Boolean
    fun loadSavedRefuelData(massUnitName: String?): RefuelSavedData?
}
