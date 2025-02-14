package com.arny.aircraftrefueling.domain.models

import com.arny.aircraftrefueling.data.utils.strings.IWrappedString

class DataThrowable(val wrappedString: IWrappedString) : Throwable()