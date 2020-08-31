package com.arny.aircraftrefueling.data.repository.files

import android.content.Context
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.constants.Consts.DIR_SD
import com.arny.aircraftrefueling.constants.Consts.FILENAME_SD
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.utils.DateTimeUtils
import com.arny.aircraftrefueling.utils.FileUtils
import com.arny.aircraftrefueling.utils.Prefs
import com.arny.aircraftrefueling.utils.sFormat
import javax.inject.Inject

class FilesRepository @Inject constructor(
        private val context: Context,
        private val prefs: Prefs,
        private val unitsRepository: IUnitsRepository
) : IFilesRepository {

    private fun getVolumeUnit(): String? = unitsRepository.getVolumeUnitName()
    private fun getMassUnit(): String? = unitsRepository.getMassUnitName()

    private fun getRefuelText(onBoard: Double, require: Double, mRo: Double, volume: Double): String {
        return DateTimeUtils.getDateTime("dd MMM yyyy HH:mm:ss") + "\n" +
                context.getString(R.string.file_fuel_remain) + "%.0f".sFormat(onBoard) + "(" + getMassUnit() + "); " +
                context.getString(R.string.file_fueled) + "%.0f".sFormat(require) + "(" + getMassUnit() + "); " +
                context.getString(R.string.unit_density) + ": " + "%.3f".sFormat(mRo) + "(" + context.getString(R.string.sh_unit_density) + "); " +
                context.getString(R.string.litre_qty) + ": " + "%.0f".sFormat(volume) + "(" + getVolumeUnit() + ")\n";
    }

    override fun saveRefuel(onBoard: Double, require: Double, mRo: Double, volume: Double): Boolean {
        val pathWithName = FileUtils.getWorkDir(context) + "/" + DIR_SD + "/"
        return FileUtils.writeToFile(getRefuelText(onBoard, require, mRo, volume), pathWithName, FILENAME_SD, true)
    }
}