package com.arny.aircraftrefueling.data.utils.strings

import android.content.Context
import com.arny.aircraftrefueling.domain.models.DataThrowable

class ThrowableString(val throwable: Throwable?) : IWrappedString {
    override fun toString(context: Context): String {
        return when (throwable) {
            is DataThrowable -> throwable.wrappedString.toString(context).orEmpty()
            else -> throwable?.message.orEmpty()
        }
    }
}