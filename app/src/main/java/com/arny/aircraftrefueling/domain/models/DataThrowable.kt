package com.arny.aircraftrefueling.domain.models

import androidx.annotation.StringRes

class DataThrowable(@StringRes val errorRes: Int) : Throwable()