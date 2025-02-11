package com.arny.aircraftrefueling.presentation.deicing

import com.arny.aircraftrefueling.data.utils.strings.IWrappedString

sealed class DeicingUIState {
    data object IDLE : DeicingUIState()
    data class CheckPVK(
        val checked: Boolean,
        val percentPvk: String
    ) : DeicingUIState()

    data class InputError(
        val boardError: Pair<Int, Int?>? = null,
        val densityError: Pair<Int, Int?>? = null,
        val percentError: Pair<Int, Int?>? = null
    ) : DeicingUIState()

    data class Result(
        val success: Boolean,
        val data: String,
        val error: IWrappedString? = null,
    ) : DeicingUIState()
}