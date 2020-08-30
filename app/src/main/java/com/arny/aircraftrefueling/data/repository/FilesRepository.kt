package com.arny.aircraftrefueling.data.repository

import android.content.Context
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.utils.DateTimeUtils
import com.arny.aircraftrefueling.utils.sFormat

class FilesRepository(private val context: Context) {
    private fun getRefuelText(onBoard: Double, require: Double, mRo: Double, volume: String): String {
        return DateTimeUtils.getDateTime("dd MMM yyyy HH:mm:ss") + "\n" +
                context.getString(R.string.file_fuel_remain) + "%.0f".sFormat(onBoard) + "(" + context.getString(R.string.sh_unit_mass) + "); " +
                context.getString(R.string.file_fueled) + "%.0f".sFormat(require) + "(" + context.getString(R.string.sh_unit_mass) + "); " +
                context.getString(R.string.unit_density) + ": " + "%.3f".sFormat(mRo) + "(" + context.getString(R.string.sh_unit_density) + "); " +
                context.getString(R.string.litre_qty) + ": " + volume + "(" + context.getString(R.string.sh_unit_volume) + ")\n";
    }
}