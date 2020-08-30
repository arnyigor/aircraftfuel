package com.arny.aircraftrefueling.presentation.deicing.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.models.DeicingResult
import com.arny.aircraftrefueling.data.models.Result
import com.arny.aircraftrefueling.presentation.deicing.presenter.DeicingPresenter
import com.arny.aircraftrefueling.utils.ToastMaker.toastError
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deicing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.menu_deicing)
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
        presenter.calculate(onBoard, density, percent)
    }

    override fun showResult(result: Result<Any>) {
        when (result) {
            is Result.Success -> showData(result.data as DeicingResult)
            is Result.Error -> toastError(requireContext(), result.throwable.message)
            is Result.ErrorRes -> toastError(requireContext(), getString(result.messageRes))
        }
    }

    private fun showData(deicingResult: DeicingResult) {
        editTotalMassFuel.setText(String.format("%.0f", deicingResult.massResult))
    }
}
