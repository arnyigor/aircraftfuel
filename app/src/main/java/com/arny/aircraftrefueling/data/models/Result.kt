package com.arny.aircraftrefueling.data.models

import androidx.annotation.StringRes

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
    data class ErrorRes(@StringRes val messageRes: Int) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$throwable]"
            is ErrorRes -> "Error with resource message"
        }
    }
}