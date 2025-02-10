package com.arny.aircraftrefueling.domain.constants

import com.arny.aircraftrefueling.domain.models.FuelTanks

object Consts {
    const val GALLON_TO_LITRE = 3.78541
    const val LITRE_TO_GALLON = 1 / GALLON_TO_LITRE
    const val LB_TO_KG = 0.45359237 //kg
    const val KG_TO_LB = 1 / LB_TO_KG
    const val WINGS_TANK_MAX_VOLUME = "WING_TANK_MAX_VOLUME"
    const val MAIN_TANK_MAX_VOLUME = "MAIN_TANK_MAX_VOLUME"
    const val NO_USE_VOLUME = "NO_USE_VOLUME"
    val fuelsData = listOf(
        FuelTanks(
            "B737",
            hashMapOf(
                WINGS_TANK_MAX_VOLUME to 4876.0,
                MAIN_TANK_MAX_VOLUME to 16273.0,
                NO_USE_VOLUME to 50.0
            )
        ),
        FuelTanks(
            "B767",
            hashMapOf(
                WINGS_TANK_MAX_VOLUME to 22751.0,
                MAIN_TANK_MAX_VOLUME to 45045.0,
                NO_USE_VOLUME to 50.0
            )
        ),
    )
    const val DIR_SD = "AirRefuelFiles"
    const val FILENAME_SD = "AirRefuelKilo.txt"
    const val ERROR_TOTAL_LESS = "error_total_less"

    const val PREF_RESP = "pref_responsibility_2020"
    const val PREF_VOLUME_UNIT = "pref_volume_unit"
    const val PREF_MASS_UNIT = "pref_mass_unit"
    const val PREF_SAVE_REFUEL_LAST_DATA = "pref_save_refuel_last_data"
    const val PREF_REFUEL_LAST_DATA_REQUIRE = "pref_refuel_last_data_req"
    const val PREF_REFUEL_LAST_DATA_BOARD = "pref_refuel_last_data_board"
    const val PREF_REFUEL_LAST_DATA_RO = "pref_refuel_last_data_ro"

    const val UNIT_LB = "lb"
    const val UNIT_KG = "kg"
    const val UNIT_LITRE = "litre"
    const val UNIT_AM_GALL = "am_gallon"

    const val CONV_LB_KG = "lb_to_kg"
    const val CONV_KG_LB = "kg_to_lb"
    const val CONV_GALL_LITRE = "gall_to_litre"
    const val CONV_LITRE_GALL = "litre_to_gall"
}
