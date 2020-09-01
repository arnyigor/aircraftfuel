package com.arny.aircraftrefueling.domain.units

import com.arny.aircraftrefueling.data.repository.units.IUnitsRepository
import com.arny.aircraftrefueling.utils.fromSingle
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UnitsInteractor @Inject constructor(
        private val unitsRepository: IUnitsRepository
) : IUnitsInteractor {

    override fun loadMassUnits(): Single<Pair<List<String>, String>> {
        return fromSingle { unitsRepository.getMassUnits() }
                .zipWith(fromSingle { unitsRepository.getMassUnit() ?: "" })
                .subscribeOn(Schedulers.io())
    }
}
