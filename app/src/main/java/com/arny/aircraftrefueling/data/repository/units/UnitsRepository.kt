package com.arny.aircraftrefueling.data.repository.units

import android.content.Context
import androidx.annotation.StringRes
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.constants.Consts.UNIT_AM_GALL
import com.arny.aircraftrefueling.constants.Consts.UNIT_KG
import com.arny.aircraftrefueling.constants.Consts.UNIT_LB
import com.arny.aircraftrefueling.constants.Consts.UNIT_LITRE
import com.arny.aircraftrefueling.domain.models.MeasureType
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.utils.Prefs
import java.math.BigDecimal
import java.math.RoundingMode
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

    private fun convertMassFromLb(mass: Double): Double {
        return (convertors[Consts.CONV_LB_KG] ?: 1.0) * mass
    }

    private fun convertVolumeFromGal(volume: Double): Double {
        return (convertors[Consts.CONV_GALL_LITRE] ?: 1.0) * volume
    }

    private fun convertMassToLb(mass: Double): Double {
        return (convertors[Consts.CONV_KG_LB] ?: 1.0) * mass
    }

    private fun convertVolumeToGal(volume: Double): Double {
        return (convertors[Consts.CONV_LITRE_GALL] ?: 1.0) * volume
    }

    override fun onMassUnitChange(unit: MeasureUnit) {
        prefs.put(Consts.PREF_MASS_UNIT, unit.name)
    }

    override fun onVolumeUnitChange(unit: MeasureUnit) {
        prefs.put(Consts.PREF_VOLUME_UNIT, unit.name)
    }

    override fun getMassCI(mass: Double, unitName: String?): Double {
        return when (unitName) {
            UNIT_LB -> convertMassFromLb(mass)
            else -> mass
        }
    }

    override fun getVolumeCI(volume: Double, unitName: String?): Double {
        return when (unitName) {
            UNIT_AM_GALL -> convertVolumeFromGal(volume)
            else -> volume
        }
    }

    override fun getMassByUnit(mass: Double, unitName: String?): Double {
        return when (unitName) {
            UNIT_LB -> convertMassToLb(mass)
            else -> mass
        }
    }


    override fun getVolumeByUnit(volume: Double, volumeDimens: String?): Double {
        return when (volumeDimens) {
            UNIT_AM_GALL -> convertVolumeToGal(volume)
            else -> volume
        }
    }

    private fun getUnitName(unitKey: String?): String? {
        @StringRes
        val resName = when (unitKey) {
            UNIT_KG -> R.string.unit_mass_kg
            UNIT_LITRE -> R.string.unit_litre
            UNIT_LB -> R.string.unit_mass_lb
            UNIT_AM_GALL -> R.string.unit_am_gallons
            else -> null
        }
        return resName?.let { context.getString(it) }
    }

    override fun getUnits(): List<MeasureUnit> {
        val mutableList = mutableListOf<MeasureUnit>()
        mutableList.addAll(
                listOf(UNIT_KG, UNIT_LB)
                        .map { MeasureUnit(it, getUnitName(it) ?: "", getSavedMassUnit() == it, MeasureType.MASS) }
        )
        mutableList.addAll(
                listOf(UNIT_LITRE, UNIT_AM_GALL)
                        .map { MeasureUnit(it, getUnitName(it) ?: "", getVolumeUnit() == it, MeasureType.VOLUME) }
        )
        return mutableList.toList()
    }

    override fun formatTo(value: Double, scale: Int): String {
        return BigDecimal(value)
                .setScale(scale, RoundingMode.HALF_UP)
                .toString()
    }

    override fun getVolumeUnits(): List<String> {
        return listOf(UNIT_LITRE, UNIT_AM_GALL)
    }

    override fun getVolumeUnit(): String {
        return prefs.get<String>(Consts.PREF_VOLUME_UNIT) ?: UNIT_LITRE
    }

    override fun getSavedMassUnit(): String {
        return prefs.get<String>(Consts.PREF_MASS_UNIT) ?: UNIT_KG
    }

    override fun getVolumeUnitName(): String? {
        return getUnitName(getVolumeUnit())
    }

    override fun getMassUnitName(): String? {
        return getUnitName(getSavedMassUnit())
    }
}