package com.arny.aircraftrefueling.data.utils.strings

import android.content.Context
import androidx.annotation.StringRes

class ResourceString(@StringRes val resString: Int, private vararg val params: Any?) :
    IWrappedString {

    override fun toString(context: Context): String? {
        return context.getString(resString, *params)
    }
}