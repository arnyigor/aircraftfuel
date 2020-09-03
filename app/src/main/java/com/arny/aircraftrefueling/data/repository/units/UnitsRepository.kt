package com.arny.aircraftrefueling.data.repository.units

import android.content.Context
import androidx.annotation.StringRes
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.constants.Consts.UNIT_AM_GALL
import com.arny.aircraftrefueling.constants.Consts.UNIT_KG
import com.arny.aircraftrefueling.constants.Consts.UNIT_LB
import com.arny.aircraftrefueling.constants.Consts.UNIT_LITRE
import com.arny.aircraftrefueling.data.models.MeasureType
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.utils.Prefs
import javax.inject.Inject

class UnitsRepository @Inject constructor(
        private val prefs: Prefs,
        private val context: Context
) : IUnitsRepository {

    private val convertors = mapOf(
            Consts.CONV_LB_KG to Consts.LB_TO_KG,
            Consts.CONV_KG_LB to Consts.KG_TO_LB,
            Consts.CONV_GALL_LITRE to Consts.GALLON_TO_LITRE,
            Consts.CONV_LITRE_GALL to Consts.LITRE_TO_GALLON
    )

    override fun onVolumeUnitChange(litre: Boolean) {
        prefs.put(Consts.PREF_VOLUME_UNIT, if (litre) UNIT_LITRE else UNIT_AM_GALL)
    }


    override fun onMassUnitChange(kg: Boolean) {
        prefs.put(Consts.PREF_MASS_UNIT, if (kg) UNIT_KG else UNIT_LB)
    }

    private fun getUnitName(unitKey: String?): String? {
        @StringRes
        val resName = when (unitKey) {
            UNIT_KG -> R.string.sh_unit_mass_kg
            UNIT_LITRE -> R.string.unit_litre
            UNIT_LB -> R.string.sh_unit_mass_lb
            UNIT_AM_GALL -> R.string.unit_am_gallons
            else -> null
        }
        return resName?.let { context.getString(it) }
    }

    override fun getUnits(): List<MeasureUnit> {
        val mutableList = mutableListOf<MeasureUnit>()
        mutableList.addAll(
                listOf(UNIT_KG, UNIT_LB)
                        .map { MeasureUnit(getUnitName(it) ?: "", getSavedMassUnit() == it, MeasureType.MASS) }
        )
        mutableList.addAll(
                listOf(UNIT_LITRE, UNIT_AM_GALL)
                        .map { MeasureUnit(getUnitName(it) ?: "", getVolumeUnit() == it, MeasureType.VOLUME) }
        )
        return mutableList.toList()
    }

    override fun getVolumeUnits(): List<String> {
        return listOf(UNIT_LITRE, UNIT_AM_GALL)
    }

    override fun getVolumeUnit(): String? {
        return prefs.get<String>(Consts.PREF_VOLUME_UNIT) ?: UNIT_LITRE
    }

    override fun getSavedMassUnit(): String? {
        return prefs.get<String>(Consts.PREF_MASS_UNIT) ?: UNIT_KG
    }

    override fun getVolumeUnitName(): String? {
        return getUnitName(getVolumeUnit())
    }

    override fun getMassUnitName(): String? {
        return getUnitName(getSavedMassUnit())
    }
}