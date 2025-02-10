package com.arny.aircraftrefueling.domain.models

data class TankRefuelResult(
    var massTotal: String = "",
    var volumeResult: String = "",
    var left: String = "",
    var right: String = "",
    var centre: String = "",
    var centreOver: Boolean = false
)
