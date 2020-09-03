package com.arny.aircraftrefueling.presentation.refuel


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.data.models.TankRefuelResult
import com.arny.aircraftrefueling.presentation.units.UnitsFragment
import com.arny.aircraftrefueling.utils.ToastMaker.toastError
import com.arny.aircraftrefueling.utils.replaceFragment
import kotlinx.android.synthetic.main.refuel_fragment.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class FragmentRefueling : MvpAppCompatFragment(), RefuelView {
    companion object {
        private const val V_UNIT_LITRE: Int = 0
        fun getInstance() = FragmentRefueling()
    }

    private val presenter by moxyPresenter { RefuelPresenter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.refuel_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(UnitsFragment.getInstance(), R.id.flMeasureContainer)
        activity?.title = getString(R.string.menu_fueling)
        buttonKiloCnt.setOnClickListener { calculateFuelCapacity() }
    }

    override fun showResult(result: Result<Any>) {
        when (result) {
            is Result.Success -> showData(result.data as TankRefuelResult)
            is Result.Error -> toastError(requireContext(), result.throwable.message)
            is Result.ErrorRes -> toastError(requireContext(), getString(result.messageRes))
        }
    }

    private fun showData(refuelResult: TankRefuelResult) {
        tvTotalLitre.text = refuelResult.volumeResult
        tvTotalKilo.text = refuelResult.massTotal
        tvLT.text = String.format("%s:\n%s", getString(R.string.left_fuel_tank), refuelResult.left)
        tvRT.text = String.format("%s:\n%s", getString(R.string.right_fuel_tank), refuelResult.right)
        tvCT.text = String.format("%s:\n%s", getString(R.string.center_fuel_tank), refuelResult.centre)
    }

    private fun calculateFuelCapacity() {
        val onBoard = editTotalMass.text.toString()
        if (onBoard.isBlank()) {
            toastError(context, getString(R.string.error_val_on_board))
            return
        }
        val required = editRequiredMass.text.toString()
        if (required.isBlank() || required == "0") {
            toastError(context, getString(R.string.error_val_req))
            return
        }
        val density = editDensityFuel.text.toString()
        if (density.isBlank() || density == "0") {
            toastError(context, getString(R.string.error_val_density))
            return
        }
        presenter.refuel(density, onBoard, required)
    }
}
