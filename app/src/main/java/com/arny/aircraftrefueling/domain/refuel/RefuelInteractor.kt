package com.arny.aircraftrefueling.domain.refuel

import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.constants.Consts.NO_USE_LITRE
import com.arny.aircraftrefueling.constants.Consts.UNIT_AM_GALL
import com.arny.aircraftrefueling.constants.Consts.UNIT_LB
import com.arny.aircraftrefueling.constants.Consts.WING_TANK_MAX_VOLUME
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.data.models.TankRefuelResult
import com.arny.aircraftrefueling.data.repository.files.IFilesRepository
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import javax.inject.Inject

class RefuelInteractor @Inject constructor(
        private val filesRepository: IFilesRepository,
        private val unitsRepository: IUnitsRepository,
) : IRefuelInteractor {
    private var massTotalStr: String = ""
    private var volumeResultStr: String = ""
    private var leftStr: String = ""
    private var rightStr: String = ""
    private var centreStr: String = ""
    override var massUnit: MeasureUnit? = null
    override var volumeUnit: MeasureUnit? = null

    private fun getMassCI(mass: Double): Double {
       return when (massUnit?.name) {
            UNIT_LB -> unitsRepository.convertMassFromLb(mass)
            else -> mass
        }
    }

    private fun getMassByUnit(mass: Double): Double {
       return when (massUnit?.name) {
            UNIT_LB -> unitsRepository.convertMassToLb(mass)
            else -> mass
        }
    }

    private fun getVolumeByUnit(volume: Double): Double {
        return when (volumeUnit?.name) {
            UNIT_AM_GALL -> unitsRepository.convertVolumeToGal(volume)
            else -> volume
        }
    }

    /**
     * Функция заправки
     *
     * @param mReq масса, сколько необходимо
     * @param mRo     массовая плотность
     * @param onBoard     масса, остаток
     */
    override fun calculate(mReq: Double, mRo: Double, mBoard: Double): TankRefuelResult {
        val massReq = getMassCI(mReq)
        val onBoard = getMassCI(mBoard)
        val maxCen: Double = getMaxCenter(mRo)
        val l: Double
        val r: Double
        val c: Double
        if (massReq <= maxCen) {
            l = massReq / 2
            r = massReq / 2
            leftStr = doubleFormat(l)
            rightStr = doubleFormat(r)
            centreStr = doubleFormat(0.0)
        } else {
            c = massReq - maxCen
            l = massReq - c
            r = l / 2
            leftStr = doubleFormat(r)
            rightStr = doubleFormat(r)
            centreStr = doubleFormat(c)
        }
        calcVolume(onBoard, mRo, massReq)
        return TankRefuelResult(massTotalStr, volumeResultStr, leftStr, rightStr, centreStr)
    }

    private fun getMaxCenter(mRo: Double) = 2 * (WING_TANK_MAX_VOLUME * mRo - NO_USE_LITRE)

    /**
     * Расчет количества литров
     */
    private fun calcVolume(onBoard: Double, mro: Double, massReq: Double) {
        val diff = massReq - onBoard
        val total = diff / mro
        massTotalStr = doubleFormat(diff)
        if (total < 0.0) {
            volumeResultStr = ""
            throw Exception(Consts.ERROR_TOTAL_LESS)
        }
        volumeResultStr = String.format("%.0f", getVolumeByUnit(total))
    }

    private fun doubleFormat(mass: Double) = String.format("%.0f", getMassByUnit(mass))
}