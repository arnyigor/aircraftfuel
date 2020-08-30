package com.arny.aircraftrefueling.presentation.refuel.presenter

import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.RefuelApp
import com.arny.aircraftrefueling.constants.Consts
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.domain.refuel.IRefuelInteractor
import com.arny.aircraftrefueling.presentation.refuel.fragment.RefuelView
import com.arny.aircraftrefueling.utils.BaseMvpPresenter
import com.arny.aircraftrefueling.utils.fromSingle
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class RefuelPresenter : BaseMvpPresenter<RefuelView>() {
    @Inject
    lateinit var interactor: IRefuelInteractor

    init {
        RefuelApp.appComponent.inject(this)
    }

    fun refuel(density: String, onBoard: String, required: String, volumeUnitType: Int) {
        interactor.vUnit = volumeUnitType
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
