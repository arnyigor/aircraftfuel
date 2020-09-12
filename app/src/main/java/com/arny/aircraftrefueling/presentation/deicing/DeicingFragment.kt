package com.arny.aircraftrefueling.presentation.deicing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.utils.KeyboardHelper.hideKeyboard
import com.arny.aircraftrefueling.utils.ToastMaker
import com.arny.aircraftrefueling.utils.ToastMaker.toastError
import com.arny.aircraftrefueling.utils.alertDialog
import kotlinx.android.synthetic.main.fragment_deicing.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter


class DeicingFragment : MvpAppCompatFragment(), DeicingView {

    companion object {
        private const val FULL_PERCENT = "100"
        private const val HALF_PERCENT = "50"
        fun getInstance() = DeicingFragment()
    }

    private val presenter by moxyPresenter { DeicingPresenter() }

    @StringRes
    private var massUnitName: Int = R.string.unit_mass_kg

    @StringRes
    private var volumeUnitName: Int = R.string.unit_volume_named

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deicing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.menu_deicing)
        buttonLitreCnt.setOnClickListener { caclMass() }
        checkPVK.setOnCheckedChangeListener { _, isChecked ->
            tilPercent.isVisible = isChecked
            editPercentPVK.isEnabled = isChecked
            if (isChecked) {
                editPercentPVK.setText(HALF_PERCENT)
            } else {
                editPercentPVK.setText(FULL_PERCENT)
            }
        }
        tiedtTotalVolume.doAfterTextChanged { tilTotalVolume.error = null }
        editDensityPVK.doAfterTextChanged { tilDensity.error = null }
        editPercentPVK.doAfterTextChanged {
            tilPercent.error = null
        }
        btnSaveToFile.setOnClickListener {
            presenter.saveData(
                    tiedtTotalVolume.text.toString(),
                    editPercentPVK.text.toString(),
                    editDensityPVK.text.toString(),
                    editTotalMassFuel.text.toString()
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
    }

    private fun caclMass() {
        val onBoard = tiedtTotalVolume.text.toString()
        if (onBoard.isBlank() || onBoard == "0") {
            tilTotalVolume.error = getString(R.string.error_deicing_volume_with_unit, getString(volumeUnitName))
            return
        }
        val density = editDensityPVK.text.toString()
        if (density.isBlank() || density == "0") {
            tilDensity.error = getString(R.string.error_val_density_pvk)
            return
        }
        val percent = editPercentPVK.text.toString()
        if (percent.isBlank() || percent == "0") {
            tilPercent.error = getString(R.string.error_val_proc_pvk)
            return
        }
        hideKeyboard(requireActivity())
        presenter.calculateDeicing(onBoard, density, percent)
    }

    override fun showResult(result: Result<Any>) {
        when (result) {
            is Result.Success -> {
                editTotalMassFuel.setText(result.data as String)
            }
            is Result.Error -> toastError(requireContext(), result.throwable.message)
            is Result.ErrorRes -> toastError(requireContext(), getString(result.messageRes))
        }
    }

    override fun toastError(errorRes: Int, message: String?) {
        toastError(requireContext(), getString(errorRes, message))
    }

    override fun toastError(message: String) {
        toastError(requireContext(), message)
    }

    override fun setMassUnitName(nameRes: Int) {
        massUnitName = nameRes
    }

    override fun setVolumeUnitName(nameRes: Int) {
        volumeUnitName = nameRes
    }

    override fun setEdtMassUnit(unitRes: Int) {
        tilTotalMass.hint = getString(R.string.total_mass_kilo, getString(unitRes))
    }

    override fun setEdtVolumeUnit(unitRes: Int) {
        tilTotalVolume.hint = getString(R.string.total_volume_litre, getString(unitRes))
    }

    override fun toastSuccess(strRes: Int, path: String?) {
        if (path.isNullOrBlank()) {
            ToastMaker.toastSuccess(requireContext(), getString(strRes))
        } else {
            ToastMaker.toastSuccess(requireContext(), getString(strRes, path))
        }
    }

    override fun setBtnDelVisible(visible: Boolean) {
        btnRemoveData.isVisible = visible
    }

    override fun setBtnSaveVisible(visible: Boolean) {
        btnSaveToFile.isVisible = visible
    }
}
