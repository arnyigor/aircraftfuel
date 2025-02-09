package com.arny.aircraftrefueling.presentation.deicing

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.databinding.FragmentDeicingBinding
import com.arny.aircraftrefueling.utils.KeyboardHelper.hideKeyboard
import com.arny.aircraftrefueling.utils.ToastMaker
import com.arny.aircraftrefueling.utils.alertDialog

class DeicingFragment : Fragment() {
    companion object {
        private const val FULL_PERCENT = "100"
        private const val HALF_PERCENT = "50"
    }
    private lateinit var binding: FragmentDeicingBinding

    private val viewModel: DeicingViewModel by viewModels()
    @StringRes
    private var massUnitName: Int = R.string.unit_mass_kg

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeicingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.menu_deicing)
        with(binding) {
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
                viewModel.saveData(
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
                        viewModel.onRemoveFile()
                    }
                )
            }
        }
    }

    private fun caclMass() = with(binding) {
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
        viewModel.calculateDeicing(onBoard, density, percent)
    }

    private fun showResult(result: Result<Any>) {
        when (result) {
            is Result.Success -> {
                binding.editTotalMassFuel.setText(result.data as String)
            }
            is Result.Error -> toastError(requireContext(), result.throwable.message)
            is Result.ErrorRes -> toastError(requireContext(), getString(result.messageRes))
        }
    }

    private fun toastError(errorRes: Int, message: String?) {
        toastError(requireContext(), getString(errorRes, message))
    }

    private fun toastError(message: String) {
        toastError(requireContext(), message)
    }

    private fun setMassUnitName(nameRes: Int) {
        massUnitName = nameRes
    }

    private fun setVolumeUnitName(nameRes: Int) {
        volumeUnitName = nameRes
    }

    private fun setEdtMassUnit(unitRes: Int) {
        binding.tilTotalMass.hint = getString(R.string.total_mass_kilo, getString(unitRes))
    }

    private fun setEdtVolumeUnit(unitRes: Int) {
        binding.tilTotalVolume.hint = getString(R.string.total_volume_litre, getString(unitRes))
    }

    private fun toastSuccess(strRes: Int, path: String?) {
        if (path.isNullOrBlank()) {
            ToastMaker.toastSuccess(requireContext(), getString(strRes))
        } else {
            ToastMaker.toastSuccess(requireContext(), getString(strRes, path))
        }
    }

    private fun setBtnDelVisible(visible: Boolean) {
        binding.btnRemoveData.isVisible = visible
    }

    private fun setBtnSaveVisible(visible: Boolean) {
        binding.btnSaveToFile.isVisible = visible
    }
}
