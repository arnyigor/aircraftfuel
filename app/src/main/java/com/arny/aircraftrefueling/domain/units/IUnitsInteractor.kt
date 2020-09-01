package com.arny.aircraftrefueling.domain.units

import io.reactivex.Single

interface IUnitsInteractor {
    fun loadMassUnits(): Single<Pair<List<String>, String>>
}
