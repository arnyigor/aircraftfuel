package com.arny.aircraftrefueling.presenter.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.models.Result
import com.arny.aircraftrefueling.presenter.viewmodel.RefuelingViewModel
import com.arny.aircraftrefueling.presenter.viewmodel.RefuelingViewModelFactory
import com.arny.aircraftrefueling.presenter.viewmodel.RerfuelUIState
import com.arny.aircraftrefueling.presenter.viewmodel.TankRefueler
import com.arny.aircraftrefueling.utils.ToastMaker.toastError
import kotlinx.android.synthetic.main.refuel_fragment.*

class FragmentRefueling : Fragment() {

    companion object {
        private const val V_UNIT_LITRE: Int = 0
        fun getInstance() = FragmentRefueling()
    }

    private var volumeUnitType: Int = 0
    private var volumeUnitName: String = ""
    private val refuelingViewModel by viewModels<RefuelingViewModel> { RefuelingViewModelFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.refuel_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVolumeSpinner()
        buttonKiloCnt.setOnClickListener { calculateFuelCapacity() }
        observeUIState()
    }

    private fun observeUIState() {
        refuelingViewModel.uiState.observe(viewLifecycleOwner, {
            when (it) {
                is RerfuelUIState.ResultState -> {
                    when (val result = it.result) {
                        is Result.Success -> showData(result.data as TankRefueler)
                        is Result.Error -> toastError(requireContext(), result.throwable.message)
                        is Result.ErrorRes -> toastError(requireContext(), getString(result.messageRes))
                    }
                }
            }
        })
    }

    private fun showData(tankRefueler: TankRefueler) {
        tvTotalLitre.text = tankRefueler.volumeResult
        tvTotalKilo.text = tankRefueler.massTotal
        tvLT.text = String.format("%s:\n%s", getString(R.string.left_fuel_tank), tankRefueler.left)
        tvRT.text = String.format("%s:\n%s", getString(R.string.right_fuel_tank), tankRefueler.right)
        tvCT.text = String.format("%s:\n%s", getString(R.string.center_fuel_tank), tankRefueler.centre)
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
        refuelingViewModel.refuel(density, onBoard, required, volumeUnitType)
/*        try {
            mRo = desnsity.toDouble()
            mMassOstat = edtTotalFuel.getText().toString().toFloat().toDouble()
            mMassRequired = edtRequiredfuel.getText().toString().toFloat().toDouble()
            val totStr: String = LitreCnt(mMassOstat, mRo, mMassRequired)
            var totLit: Double = if (totStr != "0" || totStr != "") totStr.toDouble() else 0
            if (totLit < 0) {
                Local.toast(context, getString(R.string.error_wrong_data), false)
                return
            }
            totLit = getTotLit(totLit)
            tvTotalLitre.text = String.format(Locale.getDefault(), "%.0f", totLit)
            tvTotalKilo.text = String.format(Locale.getDefault(), "%.0f", mReqMassTotal)
            tankFueling(mMassRequired, mRo)
            tvLT.text = String.format("%s:\n%s", getString(R.string.left_fuel_tank), left)
            tvRT.text = String.format("%s:\n%s", getString(R.string.right_fuel_tank), right)
            tvCT.text = String.format("%s:\n%s", getString(R.string.center_fuel_tank), centre)
            btnKiloFileSave.setVisibility(View.VISIBLE)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
    }

    private fun initVolumeSpinner() {
        spinUnits.adapter = ArrayAdapter(
                requireContext(),
                R.layout.v_unit_item,
                resources.getStringArray(R.array.volumeUnits)
        )
        spinUnits.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                volumeUnitName = adapterView.getItemAtPosition(i).toString()
                volumeUnitType = i
                tvReqHeader.text = volumeUnitName
                tvTotalLitre.text = null
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                volumeUnitName = adapterView.getItemAtPosition(V_UNIT_LITRE).toString()
                volumeUnitType = V_UNIT_LITRE
                tvReqHeader.text = volumeUnitName
                tvTotalLitre.text = null
            }
        }
    }
}
