package com.arny.aircraftrefueling.presentation.units


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.utils.ToastMaker
import kotlinx.android.synthetic.main.f_units.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class UnitsFragment : MvpAppCompatFragment(), UnitsView {
    companion object {
        fun getInstance() = UnitsFragment()
    }

    private lateinit var measureMassAdapter: MeasureUnitsAdapter
    private lateinit var measureVolumeAdapter: MeasureUnitsAdapter
    private val presenter by moxyPresenter { UnitsPresener() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_units, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.menu_settings)
        measureMassAdapter = MeasureUnitsAdapter(requireContext())
        measureVolumeAdapter = MeasureUnitsAdapter(requireContext())
        spinMassUnit.adapter = measureMassAdapter
        spinVolumeUnit.adapter = measureVolumeAdapter
    }

    override fun toastError(errorRes: Int, message: String?) {
        ToastMaker.toastError(requireContext(), getString(errorRes, message))
    }

    override fun setMassUnits(massUnits: List<MeasureUnit>, selectedIndex: Int) {
        measureMassAdapter.addAll(massUnits)
        spinMassUnit.setSelection(selectedIndex)
    }

    override fun setVolumeUnits(volumeUnits: List<MeasureUnit>, selectedIndex: Int) {
        measureVolumeAdapter.addAll(volumeUnits)
        spinVolumeUnit.setSelection(selectedIndex)
    }
}
