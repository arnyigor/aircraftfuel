package com.arny.aircraftrefueling.presentation.deicing

import androidx.annotation.StringRes
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.RefuelApp
import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.domain.models.MeasureType
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.domain.deicing.IDeicingInteractor
import com.arny.aircraftrefueling.domain.files.IFilesInteractor
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import com.arny.aircraftrefueling.utils.BaseMvpPresenter
import com.arny.aircraftrefueling.utils.fromSingle
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class DeicingPresenter : BaseMvpPresenter<DeicingView>() {
    private var massUnit: MeasureUnit? = null
    private var volumeUnit: MeasureUnit? = null

    override fun onFirstViewAttach() {
        unitsInteractor.loadUnits()
                .subscribeFromPresenter({ list ->
                    setUnitNames(list)
                }, {
                    viewState.toastError(R.string.load_units_error, it.message)
                })
        checkFileExists()
    }



}
