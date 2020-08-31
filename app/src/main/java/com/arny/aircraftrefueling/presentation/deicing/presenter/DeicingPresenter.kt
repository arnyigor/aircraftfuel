package com.arny.aircraftrefueling.presentation.deicing.presenter

import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.RefuelApp
import com.arny.aircraftrefueling.data.models.DeicingResult
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.domain.deicing.IDeicingInteractor
import com.arny.aircraftrefueling.presentation.deicing.fragment.DeicingView
import com.arny.aircraftrefueling.utils.BaseMvpPresenter
import com.arny.aircraftrefueling.utils.fromSingle
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class DeicingPresenter : BaseMvpPresenter<DeicingView>() {
    @Inject
    lateinit var interactor: IDeicingInteractor

    init {
        RefuelApp.appComponent.inject(this)
    }

    fun calculate(onBoard: String, density: String, percent: String) {
        fromSingle { getResult(onBoard, density, percent) }
                .subscribeFromPresenter({
                    viewState.showResult(it)
                }, {
                    viewState.showResult(Result.Error(it))
                })
    }


    private fun getResult(onBoard: String, density: String, percent: String): Result<DeicingResult> {
        val mRo = density.toDouble()
        if (mRo < 0.0 || mRo > 100.0) {
            return Result.ErrorRes(R.string.error_val_proc_pvk)
        }
        return Result.Success(DeicingResult(interactor.calcMass(onBoard.toDouble(), mRo, percent.toDouble())))
    }

    fun onVolumeUnitChange(checked: Boolean) {
        interactor.onVolumeUnitChange(checked)
        viewState.onVolumeChanged(if (checked) R.string.unit_am_gallons else R.string.unit_litre)
    }

    fun onMassUnitChange(checked: Boolean) {
        interactor.onMassUnitChange(checked)
        viewState.onMassChanged(if (checked) R.string.sh_unit_mass_lb else R.string.sh_unit_mass_kg)
    }
}
