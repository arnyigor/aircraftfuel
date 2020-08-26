package com.arny.aircraftrefueling.presentation.deicing.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.models.DeicingResult
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.presentation.deicing.viewmodel.DeicingUIState
import com.arny.aircraftrefueling.presentation.deicing.viewmodel.DeicingViewModel
import com.arny.aircraftrefueling.presentation.deicing.viewmodel.DeicingViewModelFactory
import com.arny.aircraftrefueling.utils.ToastMaker.toastError
import kotlinx.android.synthetic.main.fragment_deicing.*

class DeicingFragment : Fragment() {

    companion object {
        private const val FULL_PERCENT = "100"
        private const val HALF_PERCENT = "50"
        fun getInstance() = DeicingFragment()
    }

    private val deicingViewModel by viewModels<DeicingViewModel> { DeicingViewModelFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deicing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUIState()
        buttonLitreCnt.setOnClickListener { caclMass() }
        checkPVK.setOnCheckedChangeListener { _, isChecked ->
            editPercentPVK.isEnabled = isChecked
            if (isChecked) {
                editPercentPVK.setText(HALF_PERCENT)
            } else {
                editPercentPVK.setText(FULL_PERCENT)
            }
        }
    }

    private fun caclMass() {
        val onBoard = editTotalOnboard.text.toString()
        if (onBoard.isBlank() || onBoard == "0") {
            toastError(context, getString(R.string.error_val_density_pvk))
            return
        }
        val density = editDensityPVK.text.toString()
        if (density.isBlank() || density == "0") {
            toastError(context, getString(R.string.error_val_density_pvk))
            return
        }
        val percent = editPercentPVK.text.toString()
        if (percent.isBlank() || percent == "0") {
            toastError(context, getString(R.string.error_val_proc_pvk))
            return
        }
        deicingViewModel.calculate(onBoard, density, percent, checkPVK.isChecked)
    }

    private fun observeUIState() {
        deicingViewModel.uiState.observe(viewLifecycleOwner, {
            when (it) {
                is DeicingUIState.ResultState -> {
                    when (val result = it.result) {
                        is Result.Success -> showData(result.data as DeicingResult)
                        is Result.Error -> toastError(requireContext(), result.throwable.message)
                        is Result.ErrorRes -> toastError(requireContext(), getString(result.messageRes))
                    }
                }
            }
        })
    }

    private fun showData(deicingResult: DeicingResult) {
        editTotalMassFuel.setText(String.format("%.0f", deicingResult.massResult))
    }


}
