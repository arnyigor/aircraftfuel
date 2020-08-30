package com.arny.aircraftrefueling.constants

object Consts {
    private const val GALLON = 3.78541
    const val LITRE_AM_GALLON = 1 / GALLON
    const val LB = 0.45359237
    const val WING_TANK_MAX_LITRE = 4876.0
    const val NO_USE_LITRE = 50.0

    const val DIR_SD = "AirRefuelFiles"
    const val FILENAME_SD = "AirRefuelKilo.txt"

    const val ERROR_TOTAL_LESS = "error_total_less"

    const val PREF_RESP = "pref_responsibility_2020"
    const val PREF_VOLUME_UNIT = "pref_volume_unit"
    const val PREF_MASS_UNIT = "pref_mass_unit"
}