package com.arny.aircraftrefueling.presentation.refuel


import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.data.models.TankRefuelResult
import com.arny.aircraftrefueling.databinding.RefuelFragmentBinding
import com.arny.aircraftrefueling.utils.KeyboardHelper.hideKeyboard
import com.arny.aircraftrefueling.utils.ToastMaker.toastError
import com.arny.aircraftrefueling.utils.ToastMaker.toastSuccess
import com.arny.aircraftrefueling.utils.alertDialog
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import kotlin.properties.Delegates

class RefuelFragment : MvpAppCompatFragment(), RefuelView {

    companion object {
        fun getInstance() = RefuelFragment()
    }

    private lateinit var binding: RefuelFragmentBinding
    private val presenter by moxyPresenter { RefuelPresenter() }

    private var massUnitName: Int = R.string.unit_mass_kg
    private var volumeUnitName: Int = R.string.unit_volume_named

    private var selectedType by Delegates.observable("", { _, old, new ->
        if (old != new) {
            activity?.title = "${getString(R.string.menu_fueling)} ${new}"
        }
    })

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RefuelFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.menu_fueling)
        with(binding) {
            btnCalculate.setOnClickListener { calculateFuelCapacity() }
            btnSaveToFile.setOnClickListener {
                presenter.saveData(
                    tiEdtRecodData.text.toString(),
                    editTotalMass.text.toString(),
                    editRequiredMass.text.toString(),
                    editDensityFuel.text.toString(),
                    tvTotalLitre.text.toString(),
                )
            }
            btnRemoveData.setOnClickListener {
                alertDialog(
                    requireContext(),
                    title = getString(R.string.file_del) + "?",
                    btnOkText = getString(android.R.string.ok),
                    btnCancelText = getString(android.R.string.cancel),
                    onConfirm = {
                        presenter.onRemoveFile()
                    }
                )
            }
            editDensityFuel.doAfterTextChanged { s ->
                if (s.toString().contains(",")) {
                    s?.replace(
                        0,
                        s.length,
                        SpannableStringBuilder(s.toString().replace(",", "."))
                    )
                }
            }
            spinAircraftType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedType = spinAircraftType.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }

    override fun setEdtRequire(mReq: String?) {
        binding.editRequiredMass.setText(mReq)
    }

    override fun setEdtRo(mRo: String?) {
        binding.editDensityFuel.setText(mRo)
    }

    override fun setEdtBoard(onBoard: String?) {
        binding.editTotalMass.setText(onBoard)
    }

    override fun showResult(result: Result<Any>) {
        when (result) {
            is Result.Success -> showData(result.data as TankRefuelResult)
            is Result.Error -> toastError(requireContext(), result.throwable.message)
            is Result.ErrorRes -> toastError(requireContext(), getString(result.messageRes))
        }
    }

    private fun showData(refuelResult: TankRefuelResult) = with(binding) {
        tvTotalLitre.text = refuelResult.volumeResult
        tvTotalKilo.text = refuelResult.massTotal
        tvLT.text = String.format("%s:\n%s", getString(R.string.left_fuel_tank), refuelResult.left)
        tvRT.text = String.format("%s:\n%s", getString(R.string.right_fuel_tank), refuelResult.right)
        tvCT.text = String.format("%s:\n%s", getString(R.string.center_fuel_tank), refuelResult.centre)
        TextViewCompat.setTextAppearance(
            tvCT,
            if (refuelResult.centreOver) R.style.TankInfoError else R.style.TankInfoDefault
        )
    }

    private fun calculateFuelCapacity() = with(binding) {
        val onBoard = editTotalMass.text.toString()
        if (onBoard.isBlank()) {
            toastError(context, getString(R.string.error_val_on_board_with_unit, getString(massUnitName)))
            return
        }
        val required = editRequiredMass.text.toString()
        if (required.isBlank() || required == "0") {
            toastError(context, getString(R.string.error_val_req, getString(massUnitName)))
            return
        }
        val density = editDensityFuel.text.toString()
        if (density.isBlank() || density == "0") {
            toastError(context, getString(R.string.error_val_density))
            return
        }
        hideKeyboard(requireActivity())
        val type = spinAircraftType.getItemAtPosition(spinAircraftType.selectedItemPosition).toString()
        presenter.refuel(density, onBoard, required, type)
    }

    override fun setMassUnitName(nameRes: Int) {
        massUnitName = nameRes
    }

    override fun setVolumeUnitName(nameRes: Int) {
        volumeUnitName = nameRes
    }

    override fun toastError(errorRes: Int, message: String?) {
        toastError(requireContext(), getString(errorRes, message))
    }

    override fun toastError(message: String) {
        toastError(requireContext(), message)
    }

    override fun setTotalMassUnit(unitRes: Int) {
        binding.tilOnBoard.hint = getString(R.string.has_on_board, getString(unitRes))
    }

    override fun setOstatMassUnit(unitRes: Int) {
        binding.tvOstatMass.text = getText(unitRes)
    }

    override fun setReqMassUnit(unitRes: Int) {
        binding.tilReq.hint = getString(R.string.required_kilo, getString(unitRes))
    }

    override fun setOstatVolumeUnit(unitRes: Int) {
        binding.tvReqHeader.text = getString(unitRes)
    }

    override fun toastSuccess(strRes: Int, path: String?) {
        if (path.isNullOrBlank()) {
            toastSuccess(requireContext(), getString(strRes))
        } else {
            toastSuccess(requireContext(), getString(strRes, path))
        }
    }

    override fun setBtnDelVisible(visible: Boolean) {
        binding.btnRemoveData.isVisible = visible
    }

    override fun setBtnSaveVisible(visible: Boolean) {
        binding.btnSaveToFile.isVisible = visible
    }
}
