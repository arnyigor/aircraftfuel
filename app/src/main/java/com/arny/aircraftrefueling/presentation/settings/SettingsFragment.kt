package com.arny.aircraftrefueling.presentation.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.utils.ToastMaker
import kotlinx.android.synthetic.main.f_units.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class SettingsFragment : MvpAppCompatFragment(), SettingsView {
    companion object {
        fun getInstance() = SettingsFragment()
    }

    private lateinit var measureMassAdapter: MeasureUnitsAdapter
    private lateinit var measureVolumeAdapter: MeasureUnitsAdapter
    private val presenter by moxyPresenter { SettingsPresener() }

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

        spinVolumeUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                measureVolumeAdapter.items.forEach { it.selected = false }
                presenter.onVolumeUnitChange(measureVolumeAdapter.getItem(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        spinMassUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                measureMassAdapter.items.forEach { it.selected = false }
                presenter.onMassUnitChange(measureMassAdapter.getItem(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
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
