package com.arny.aircraftrefueling.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.databinding.FUnitsBinding
import com.arny.aircraftrefueling.presentation.deicing.DeicingViewModel
import com.arny.aircraftrefueling.utils.ToastMaker

class SettingsFragment : Fragment() {
    private val viewModel: DeicingViewModel by viewModels()
    private lateinit var binding: FUnitsBinding
    private lateinit var measureMassAdapter: MeasureUnitsAdapter
    private lateinit var measureVolumeAdapter: MeasureUnitsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FUnitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.menu_settings)
        measureMassAdapter = MeasureUnitsAdapter(requireContext())
        measureVolumeAdapter = MeasureUnitsAdapter(requireContext())
        with(binding) {
            spinMassUnit.adapter = measureMassAdapter
            spinVolumeUnit.adapter = measureVolumeAdapter
            spinVolumeUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    measureVolumeAdapter.items.forEach { it.selected = false }
                    viewModel.onVolumeUnitChange(measureVolumeAdapter.getItem(position))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            spinMassUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    measureMassAdapter.items.forEach { it.selected = false }
                    viewModel.onMassUnitChange(measureMassAdapter.getItem(position))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }

    private fun toastError(errorRes: Int, message: String?) {
        ToastMaker.toastError(requireContext(), getString(errorRes, message))
    }

    private fun setMassUnits(massUnits: List<MeasureUnit>, selectedIndex: Int) {
        measureMassAdapter.addAll(massUnits)
        binding.spinMassUnit.setSelection(selectedIndex)
    }

    private fun setVolumeUnits(volumeUnits: List<MeasureUnit>, selectedIndex: Int) {
        measureVolumeAdapter.addAll(volumeUnits)
        binding.spinVolumeUnit.setSelection(selectedIndex)
    }
}
