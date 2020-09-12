package com.arny.aircraftrefueling.domain.files

import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.data.repository.files.IFilesRepository
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.domain.models.RefuelSavedData
import com.arny.aircraftrefueling.utils.Prefs
import javax.inject.Inject

class FilesInteractor @Inject constructor(
        private val filesRepository: IFilesRepository,
        private val unitsRepository: IUnitsRepository,
        private val prefs: Prefs
) : IFilesInteractor {

    override fun removeFile(): Boolean {
        return filesRepository.removeFile()
    }

    override fun loadSavedRefuelData(massUnitName: String?): RefuelSavedData? {
        if (prefs.get<Boolean>(Consts.PREF_SAVE_REFUEL_LAST_DATA) == true) {
            val reqFormatted = prefs.get<String>(Consts.PREF_REFUEL_LAST_DATA_REQUIRE)
                    ?.let { formatMassToInt(it.toDouble(), massUnitName) }
            val boardFormatted = prefs.get<String>(Consts.PREF_REFUEL_LAST_DATA_BOARD)
                    ?.let { formatMassToInt(it.toDouble(), massUnitName) }
            val roFormatted = prefs.get<String>(Consts.PREF_REFUEL_LAST_DATA_RO)
                    ?.let { unitsRepository.formatTo(it.toDouble(), it.substringAfterLast(".").length) }
            return RefuelSavedData(reqFormatted, boardFormatted, roFormatted)
        }
        return null
    }

    override fun isDataFileExists(): Boolean {
        return filesRepository.isDataFileExists()
    }

    private fun formatMassToInt(mass: Double, unitName: String?): String {
        return unitsRepository.formatTo(unitsRepository.getMassByUnit(mass, unitName))
    }
}