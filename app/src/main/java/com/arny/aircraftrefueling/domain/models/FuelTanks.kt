package com.arny.aircraftrefueling.domain.models

data class FuelTanks(
    val aircraftType: String,
    val config: HashMap<String, Double>
)