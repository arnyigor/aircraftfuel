package com.arny.aircraftrefueling.presentation.refuel

import com.arny.aircraftrefueling.data.utils.strings.IWrappedString
import com.arny.aircraftrefueling.domain.models.TankRefuelResult

sealed class RefuelUIState {
    data object IDLE : RefuelUIState()

    data class InputError(
        val boardError: Pair<Int, Int?>? = null,
        val requiredError: Pair<Int, Int?>? = null,
        val densityError: Pair<Int, Int?>? = null
    ) : RefuelUIState()

    data class Result(
        val success: Boolean,
        val data: TankRefuelResult? = null,
        val error: IWrappedString? = null,
    ) : RefuelUIState()
}