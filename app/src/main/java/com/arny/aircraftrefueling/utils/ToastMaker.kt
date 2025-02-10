package com.arny.aircraftrefueling.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun toast(context: Context?, message: String?) {
    Handler(Looper.getMainLooper()).post { Toast.makeText(context, message, Toast.LENGTH_LONG).show() }
}

fun toastError(context: Context, message: String) {
    Handler(Looper.getMainLooper()).post { Toasty.error(context, message, Toast.LENGTH_LONG).show() }
}

fun toastInfo(context: Context, message: String) {
    Toasty.info(context, message, Toast.LENGTH_LONG).show()
}

fun toastSuccess(context: Context, message: String) {
    Handler(Looper.getMainLooper()).post { Toasty.success(context, message, Toast.LENGTH_LONG).show() }
}