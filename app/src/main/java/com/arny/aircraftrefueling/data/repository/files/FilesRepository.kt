package com.arny.aircraftrefueling.data.repository.files

import android.content.Context
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.constants.Consts.DIR_SD
import com.arny.aircraftrefueling.constants.Consts.FILENAME_SD
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.utils.DateTimeUtils
import com.arny.aircraftrefueling.utils.FileUtils
import com.arny.aircraftrefueling.utils.Prefs
import javax.inject.Inject

class FilesRepository @Inject constructor(
        private val context: Context,
        private val prefs: Prefs,
        private val unitsRepository: IUnitsRepository
) : IFilesRepository {

    private fun getVolumeUnit(): String? = unitsRepository.getVolumeUnitName()
    private fun getMassUnit(): String? = unitsRepository.getMassUnitName()

    private fun getRefuelText(
            recordData: String?,
            onBoard: String,
            require: String,
            mRo: String,
            volume: String
    ): String {
        val rData = if (!recordData.isNullOrBlank()) {
            context.getString(R.string.record_data_title, recordData) + "\n"
        } else {
            ""
        }
        return "\n" + DateTimeUtils.getDateTime("dd MMM yyyy HH:mm:ss") + "\n" + rData +
                context.getString(R.string.file_fuel_remain) + ": $onBoard(${getMassUnit()});\n" +
                context.getString(R.string.file_fueled) + ": $require(${getMassUnit()});\n" +
                context.getString(R.string.density) + ": $mRo(${context.getString(R.string.unit_density)});\n" +
                context.getString(R.string.litre_qty) + ": $volume(${getVolumeUnit()})\n";
    }

    override fun saveRefuel(
            recordData: String?,
            onBoard: String,
            require: String,
            mRo: String,
            volume: String
    ): String {
        val pathWithName = FileUtils.getWorkDir(context) + "/" + DIR_SD + "/"
        val writeToFile = FileUtils.writeToFile(
                getRefuelText(recordData, onBoard, require, mRo, volume),
                pathWithName,
                FILENAME_SD,
                true
        )
        return if (writeToFile) pathWithName else ""
    }
}