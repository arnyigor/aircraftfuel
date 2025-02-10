package com.arny.aircraftrefueling.presentation.refuel

import androidx.lifecycle.ViewModel
import com.arny.aircraftrefueling.domain.files.IFilesInteractor
import com.arny.aircraftrefueling.domain.refuel.IRefuelInteractor
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import dagger.assisted.AssistedInject

class RefuelViewModel @AssistedInject constructor(
    private val interactor: IRefuelInteractor,
    private val filesInteractor: IFilesInteractor,
    private val unitsInteractor: IUnitsInteractor,
) : ViewModel() {

}