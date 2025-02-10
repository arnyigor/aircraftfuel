package com.arny.aircraftrefueling.data.utils.strings

import android.content.Context

interface IWrappedString {
    fun toString(context: Context): String?
}