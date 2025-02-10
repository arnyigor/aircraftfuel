package com.arny.aircraftrefueling.data.utils.strings

import android.content.Context

class SimpleString(val string: String?) : IWrappedString {
    override fun toString(context: Context): String? = string
}