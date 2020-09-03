package com.arny.aircraftrefueling.domain.refuel

import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.constants.Consts.LITRE_TO_GALLON
import com.arny.aircraftrefueling.constants.Consts.NO_USE_LITRE
import com.arny.aircraftrefueling.constants.Consts.WING_TANK_MAX_VOLUME
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.data.models.TankRefuelResult
import com.arny.aircraftrefueling.data.repository.files.IFilesRepository
import java.util.*
import javax.inject.Inject

class RefuelInteractor @Inject constructor(
        private val filesRepository: IFilesRepository,
) : IRefuelInteractor {
    private var massTotal: String = ""
    private var volumeResult: String = ""
    private var left: String = ""
    private var right: String = ""
    private var centre: String = ""
    override var massUnit: MeasureUnit? = null
    override var volumeUnit: MeasureUnit? = null

    /**
     * Функция заправки
     *
     * @param massReq масса, сколько необходимо
     * @param mRo     массовая плотность
     * @param onBoard     масса, остаток
     */
    override fun calculate(massReq: Double, mRo: Double, onBoard: Double): TankRefuelResult {
        val maxCen: Double = 2 * (WING_TANK_MAX_VOLUME * mRo - NO_USE_LITRE)
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
        return TankRefuelResult(massTotal, volumeResult, left, right, centre)
    }

    private fun getMeasuredVolume(volume: Double): Double {
        var totVolume = volume
        when (volumeUnit?.name) {
            Consts.UNIT_AM_GALL -> {
                totVolume *= LITRE_TO_GALLON
            }
        }
        return totVolume
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
            throw Exception(Consts.ERROR_TOTAL_LESS)
        }
        volumeResult = String.format(Locale.getDefault(), "%.0f", getMeasuredVolume(total))
    }
}