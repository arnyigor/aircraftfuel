package com.arny.aircraftrefueling.presenter.viewmodel

import com.arny.aircraftrefueling.constants.Consts.LITRE_AM_GALLON
import com.arny.aircraftrefueling.constants.Consts.NO_USE_LITRE
import com.arny.aircraftrefueling.constants.Consts.WING_TANK_MAX_LITRE
import java.util.*

class TankRefueler {
    var massTotal: String = ""
    var volumeResult: String = ""
    var left: String = ""
    var right: String = ""
    var centre: String = ""
    var vUnit: Int = 0

    /**
     * Функция заправки
     *
     * @param massReq масса, сколько необходимо
     * @param mRo     массовая плотность
     * @param onBoard     масса, остаток
     */
    fun calculate(massReq: Double, mRo: Double, onBoard: Double) {
        val maxCen: Double = 2 * (WING_TANK_MAX_LITRE * mRo - NO_USE_LITRE)
        val l: Double
        val r: Double
        val c: Double
        if (massReq <= maxCen) {
            l = massReq / 2
            r = massReq / 2
            left = String.format("%.0f", l)
            right = String.format("%.0f", r)
            centre = String.format("%d", 0)
        } else {
            c = massReq - maxCen
            l = massReq - c
            r = l / 2
            left = String.format("%.0f", r)
            right = String.format("%.0f", r)
            centre = String.format("%.0f", c)
        }
        calcVolume(onBoard, mRo, massReq)
    }

    private fun getTotLit(totLitre: Double): Double {
        var totLit = totLitre
        if (vUnit == 1) {
            totLit *= LITRE_AM_GALLON
        }
        return totLit
    }

    /**
     * Расчет количества литров
     */
    private fun calcVolume(onBoard: Double, mro: Double, massReq: Double) {
        val diff = massReq - onBoard
        val total = diff / mro
        massTotal = String.format("%.0f", diff)
        if (total < 0.0) {
            volumeResult = ""
            return
        }
        volumeResult = String.format(Locale.getDefault(), "%.0f", getTotLit(total))
    }
}