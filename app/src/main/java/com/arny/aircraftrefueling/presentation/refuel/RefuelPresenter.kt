package com.arny.aircraftrefueling.presentation.refuel

import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.RefuelApp
import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.domain.refuel.IRefuelInteractor
import com.arny.aircraftrefueling.domain.units.IUnitsInteractor
import com.arny.aircraftrefueling.utils.BaseMvpPresenter
import com.arny.aircraftrefueling.utils.fromSingle
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class RefuelPresenter : BaseMvpPresenter<RefuelView>() {
    private var massUnit: MeasureUnit? = null
    private var volumeUnit: MeasureUnit? = null

    @Inject
    lateinit var interactor: IRefuelInteractor

    @Inject
    lateinit var unitsInteractor: IUnitsInteractor

    init {
        RefuelApp.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        unitsInteractor.getMassUnitObs()
                .subscribeFromPresenter({
                    massUnit = it
                    println("getMassUnitObs :$it")
                }, {
                    it.printStackTrace()
                })
        unitsInteractor.getVolumeUnitObs()
                .subscribeFromPresenter({
                    volumeUnit = it
                    println("getVolumeUnitObs :$it")
                }, {
                    it.printStackTrace()
                })
    }

    fun refuel(density: String, onBoard: String, required: String) {
        interactor.volumeUnit = volumeUnit
        interactor.massUnit = massUnit
        fromSingle {
            interactor.calculate(
                    required.toDouble(),
                    density.toDouble(),
                    onBoard.toDouble()
            )
        }.subscribeFromPresenter({
            viewState.showResult(Result.Success(it))
        }, { e ->
            if (e.message == Consts.ERROR_TOTAL_LESS) {
                viewState.showResult(Result.ErrorRes(R.string.error_val_on_board))
            } else {
                viewState.showResult(Result.Error(e))
            }
        })
    }
}
