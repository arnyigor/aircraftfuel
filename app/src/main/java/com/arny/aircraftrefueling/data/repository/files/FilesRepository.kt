package com.arny.aircraftrefueling.data.repository.files

import android.content.Context
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.constants.Consts.DIR_SD
import com.arny.aircraftrefueling.constants.Consts.FILENAME_SD
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.utils.DateTimeUtils
import com.arny.aircraftrefueling.utils.FileUtils
import java.io.File
import javax.inject.Inject

class FilesRepository @Inject constructor(
        private val context: Context,
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
        return "\n" + context.getString(R.string.fueling) + "\n" +
                getCurrentDateTime() + "\n" + getRecordData(recordData) +
                context.getString(R.string.file_fuel_remain) + ": $onBoard(${getMassUnit()});\n" +
                context.getString(R.string.file_fueled) + ": $require(${getMassUnit()});\n" +
                context.getString(R.string.density) + ": $mRo(${context.getString(R.string.unit_density)});\n" +
                context.getString(R.string.litre_qty) + ": $volume(${getVolumeUnit()})\n"
    }

    private fun getRecordData(recordData: String?) = if (!recordData.isNullOrBlank())
        context.getString(R.string.record_data_title, recordData) + "\n" else ""

    private fun getDeicingData(
            recordData: String?,
            mVolTotal: String,
            mPercPVK: String,
            mRo: String,
            totalMass: String
    ): String {
        return "\n" + context.getString(R.string.deicing) + "\n" +
                getCurrentDateTime() + "\n" + getRecordData(recordData) +
                context.getString(R.string.litre_qty) + ": $mVolTotal(${getVolumeUnit()});\n" +
                context.getString(R.string.file_percent_pvk) + ": $mPercPVK(%);\n" +
                context.getString(R.string.file_density_pvk) + ": $mRo(${context.getString(R.string.unit_density)});\n" +
                context.getString(R.string.file_total_mass) + ": $totalMass(${getMassUnit()})\n"
    }

    private fun getCurrentDateTime() = DateTimeUtils.getDateTime("dd MMM yyyy HH:mm:ss")

    override fun isDataFileExists() = FileUtils.isFileExist(folderPath() + File.separator + FILENAME_SD)

    override fun saveDeicingData(
            recordData: String?,
            mVolTotal: String,
            mPercPVK: String,
            mRo: String,
            totalMass: String
    ): String {
        return saveData(getDeicingData(recordData, mVolTotal, mPercPVK, mRo, totalMass))
    }

    override fun saveRefuelData(
            recordData: String?,
            onBoard: String,
            require: String,
            mRo: String,
            volume: String
    ) = saveData(getRefuelText(recordData, onBoard, require, mRo, volume))

    private fun saveData(data: String): String {
        val folderPath = folderPath()
        return if (FileUtils.writeToFile(
                        data,
                        folderPath,
                        FILENAME_SD,
                        true
                )) folderPath + File.separator + FILENAME_SD else ""
    }

    private fun folderPath() = FileUtils.getWorkDir(context) + File.separator + DIR_SD

    override fun removeFile(): Boolean {
        FileUtils.deleteFile(File(folderPath() + File.separator + FILENAME_SD))
        return isDataFileExists()
    }
}
