package com.arny.aircraftrefueling.data.utils.strings

import android.content.Context

class ParametricString(private val format: String, private vararg val params: Any?) :
    IWrappedString {
    override fun toString(context: Context): String = try {
        String.format(format, *params)
    } catch (e: Exception) {
       ""
    }
}
