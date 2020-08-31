package com.arny.aircraftrefueling.constants

object Consts {
    const val GALLON_TO_LITRE = 3.78541
    const val LITRE_TO_GALLON = 1 / GALLON_TO_LITRE
    const val LB_TO_KG = 0.45359237 //kg
    const val KG_TO_LB = 1 / LB_TO_KG
    const val WING_TANK_MAX_LITRE = 4876.0
    const val NO_USE_LITRE = 50.0

    const val DIR_SD = "AirRefuelFiles"
    const val FILENAME_SD = "AirRefuelKilo.txt"
    const val ERROR_TOTAL_LESS = "error_total_less"

    const val PREF_RESP = "pref_responsibility_2020"
    const val PREF_VOLUME_UNIT = "pref_volume_unit"
    const val PREF_MASS_UNIT = "pref_mass_unit"

    const val UNIT_LB = "lb"
    const val UNIT_KG = "kg"
    const val UNIT_LITRE = "litre"
    const val UNIT_AM_GALL = "am_gallon"

    const val CONV_LB_KG = "lb_to_kg"
    const val CONV_KG_LB = "kg_to_lb"
    const val CONV_GALL_LITRE = "gall_to_litre"
    const val CONV_LITRE_GALL = "litre_to_gall"
}
