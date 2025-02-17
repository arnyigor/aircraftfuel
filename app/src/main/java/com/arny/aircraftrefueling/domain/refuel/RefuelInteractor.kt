package com.arny.aircraftrefueling.domain.refuel

import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.repository.Prefs
import com.arny.aircraftrefueling.data.repository.files.IFilesRepository
import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.data.utils.DataResult
import com.arny.aircraftrefueling.data.utils.doAsync
import com.arny.aircraftrefueling.data.utils.strings.ResourceString
import com.arny.aircraftrefueling.data.utils.strings.SimpleString
import com.arny.aircraftrefueling.domain.constants.Consts
import com.arny.aircraftrefueling.domain.constants.Consts.PREF_REFUEL_LAST_DATA_BOARD
import com.arny.aircraftrefueling.domain.constants.Consts.PREF_REFUEL_LAST_DATA_REQUIRE
import com.arny.aircraftrefueling.domain.constants.Consts.PREF_REFUEL_LAST_DATA_RO
import com.arny.aircraftrefueling.domain.constants.Consts.PREF_SAVE_REFUEL_LAST_DATA
import com.arny.aircraftrefueling.domain.constants.Consts.fuelsData
import com.arny.aircraftrefueling.domain.models.DataThrowable
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.domain.models.TankRefuelResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RefuelInteractor @Inject constructor(
    private val filesRepository: IFilesRepository,
    private val unitsRepository: IUnitsRepository,
    private val prefs: Prefs,
) : IRefuelInteractor {
    private var massTotalStr: String = ""
    private var volumeResultStr: String = ""
    private var leftStr: String = ""
    private var rightStr: String = ""
    private var centreStr: String = ""
    override var massUnit: MeasureUnit? = null
    override var volumeUnit: MeasureUnit? = null

    override suspend fun saveData(
        recordData: String,
        onBoard: String,
        require: String,
        density: String,
        volume: String
    ): String = filesRepository.saveRefuelData(recordData, onBoard, require, density, volume)

    /**
     * @param mReq масса, сколько необходимо
     * @param mRo     массовая плотность
     * @param onBoard     масса, остаток
     */
    override suspend fun calculateRefuel(
        mReq: Double,
        mRo: Double,
        mBoard: Double,
        type: String
    ): Flow<DataResult<TankRefuelResult>> = doAsync {
        val aircraftType = fuelsData.find { it.aircraftType == type }
        if (aircraftType == null) {
            throw DataThrowable(ResourceString(R.string.error_no_type_data))
        }
        val massReq: Double = unitsRepository.getMassCI(mReq, massUnit?.name)
        val onBoard: Double = unitsRepository.getMassCI(mBoard, massUnit?.name)
        if (prefs.get<Boolean>(PREF_SAVE_REFUEL_LAST_DATA) == true) {
            prefs.put(PREF_REFUEL_LAST_DATA_REQUIRE, massReq.toString())
            prefs.put(PREF_REFUEL_LAST_DATA_BOARD, onBoard.toString())
            prefs.put(PREF_REFUEL_LAST_DATA_RO, mRo.toString().replace(",", "."))
        }
        val oneWingMaxVolume = aircraftType.config[Consts.WINGS_TANK_MAX_VOLUME] ?: 0.0
        val noUseVolume = aircraftType.config[Consts.NO_USE_VOLUME] ?: 0.0
        val centreTankMaxVolume = aircraftType.config[Consts.MAIN_TANK_MAX_VOLUME] ?: 0.0
        val maxCen: Double = getMaxCenter(mRo, noUseVolume, oneWingMaxVolume)
        val l: Double
        val r: Double
        val c: Double
        var centreOver = false
        if (massReq <= maxCen) {
            l = massReq / 2
            r = massReq / 2
            leftStr = formatMassToInt(l)
            rightStr = formatMassToInt(r)
            centreStr = formatMassToInt(0.0)
        } else {
            c = massReq - maxCen
            l = massReq - c
            r = l / 2
            leftStr = formatMassToInt(r)
            rightStr = formatMassToInt(r)
            centreStr = formatMassToInt(c)
            centreOver = c - centreTankMaxVolume >= 0.0
        }
        calcVolume(onBoard, mRo, massReq)
        TankRefuelResult(massTotalStr, volumeResultStr, leftStr, rightStr, centreStr, centreOver)
    }

    private fun getMaxCenter(
        mRo: Double,
        noUseLItre: Double,
        wingMaxVolume: Double
    ) = 2 * (wingMaxVolume * mRo - noUseLItre)

    /**
     * Расчет количества литров
     */
    private fun calcVolume(onBoard: Double, mro: Double, massReq: Double) {
        val diff = massReq - onBoard
        val total = diff / mro
        massTotalStr = formatMassToInt(diff)
        if (total <= 0.0) {
            volumeResultStr = ""
            throw DataThrowable(SimpleString(Consts.ERROR_TOTAL_LESS))
        }
        volumeResultStr =
            unitsRepository.formatTo(unitsRepository.getVolumeByUnit(total, volumeUnit?.name))
    }

    private fun formatMassToInt(mass: Double): String =
        unitsRepository.formatTo(unitsRepository.getMassByUnit(mass, massUnit?.name))
}