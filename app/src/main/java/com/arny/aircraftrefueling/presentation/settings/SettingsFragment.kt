package com.arny.aircraftrefueling.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.utils.launchWhenCreated
import com.arny.aircraftrefueling.data.utils.strings.IWrappedString
import com.arny.aircraftrefueling.databinding.FUnitsBinding
import com.arny.aircraftrefueling.di.viewModelFactory
import com.arny.aircraftrefueling.domain.models.MeasureUnit
import com.arny.aircraftrefueling.utils.KeyboardHelper.hideKeyboard
import com.arny.aircraftrefueling.utils.ToastMaker
import dagger.android.support.AndroidSupportInjection
import dagger.assisted.AssistedFactory
import javax.inject.Inject

class SettingsFragment : Fragment() {
    @AssistedFactory
    internal interface ViewModelFactory {
        fun create(): SettingsViewModel
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SettingsViewModel by viewModelFactory { viewModelFactory.create() }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

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
        initUI()
        observeData()
    }

    private fun observeData() {
        launchWhenCreated { viewModel.toastError.collect(::toastError) }
        launchWhenCreated { viewModel.massUnits.collect(::setMassUnits) }
        launchWhenCreated { viewModel.volumeUnits.collect(::setVolumeUnits) }
        launchWhenCreated { viewModel.hideKeyboard.collect { hideKeyboard(requireActivity()) } }
    }

    private fun initUI() {
        measureMassAdapter = MeasureUnitsAdapter(requireContext())
        measureVolumeAdapter = MeasureUnitsAdapter(requireContext())
        with(binding) {
            spinMassUnit.adapter = measureMassAdapter
            spinVolumeUnit.adapter = measureVolumeAdapter
            spinVolumeUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    measureVolumeAdapter.items.forEach { it.selected = false }
                    viewModel.onVolumeUnitChange(measureVolumeAdapter.getItem(position))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            spinMassUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    measureMassAdapter.items.forEach { it.selected = false }
                    viewModel.onMassUnitChange(measureMassAdapter.getItem(position))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }

    private fun toastError(wrappedString: IWrappedString?) {
        wrappedString?.toString(requireContext())?.let {
            ToastMaker.toastError(requireContext(), it)
        }
    }

    private fun setMassUnits(massUnits: List<MeasureUnit>) {
        measureMassAdapter.addAll(massUnits)
        binding.spinMassUnit.setSelection(massUnits.indexOfFirst { it.selected })
    }

    private fun setVolumeUnits(volumeUnits: List<MeasureUnit>) {
        measureVolumeAdapter.addAll(volumeUnits)
        binding.spinVolumeUnit.setSelection(volumeUnits.indexOfFirst { it.selected })
    }
}
