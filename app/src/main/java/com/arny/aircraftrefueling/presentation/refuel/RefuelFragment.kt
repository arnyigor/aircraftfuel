package com.arny.aircraftrefueling.presentation.refuel


import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.utils.launchWhenCreated
import com.arny.aircraftrefueling.data.utils.strings.IWrappedString
import com.arny.aircraftrefueling.databinding.RefuelFragmentBinding
import com.arny.aircraftrefueling.di.viewModelFactory
import com.arny.aircraftrefueling.domain.models.TankRefuelResult
import com.arny.aircraftrefueling.utils.KeyboardHelper.hideKeyboard
import com.arny.aircraftrefueling.utils.alertDialog
import com.arny.aircraftrefueling.utils.toastError
import com.arny.aircraftrefueling.utils.toastSuccess
import com.google.android.material.textfield.TextInputLayout
import dagger.assisted.AssistedFactory
import javax.inject.Inject
import kotlin.properties.Delegates

class RefuelFragment : Fragment() {

    private lateinit var binding: RefuelFragmentBinding

    private var selectedType by Delegates.observable("") { _, old, new ->
        if (old != new) {
            activity?.title = "${getString(R.string.menu_fueling)} $new"
        }
    }

    @AssistedFactory
    internal interface ViewModelFactory {
        fun create(): RefuelViewModel
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: RefuelViewModel by viewModelFactory { viewModelFactory.create() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RefuelFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.menu_fueling)
        setListeners()
        observeData()
    }

    private fun observeData() {
        launchWhenCreated { viewModel.refuelUIState.collect(::updateState) }
        launchWhenCreated { viewModel.toastError.collect(::toastError) }
        launchWhenCreated { viewModel.toastSuccess.collect(::toastSuccess) }
        launchWhenCreated { viewModel.btnDelVisible.collect(::setBtnDelVisible) }
        launchWhenCreated { viewModel.btnSaveVisible.collect(::setBtnSaveVisible) }
        launchWhenCreated { viewModel.edtMassUnit.collect(::setEdtMassUnit) }
        launchWhenCreated { viewModel.edtVolumeUnit.collect(::setEdtVolumeUnit) }
        launchWhenCreated { viewModel.hideKeyboard.collect { hideKeyboard(requireActivity()) } }
    }

    private fun setListeners() {
        with(binding) {
            btnCalculate.setOnClickListener {
                val onBoard = editTotalMass.text.toString()
                val type = spinAircraftType.getItemAtPosition(
                    spinAircraftType.selectedItemPosition
                ).toString()
                val required = editRequiredMass.text.toString()
                val density = editDensityFuel.text.toString()
                viewModel.calculateFuelCapacity(onBoard, required, density, type)
            }
            btnSaveToFile.setOnClickListener {
                viewModel.saveData(
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
                        viewModel.onRemoveFile()
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
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedType = spinAircraftType.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }

    private fun updateState(state: RefuelUIState) = with(binding) {
        when (state) {
            RefuelUIState.IDLE -> {}

            is RefuelUIState.InputError -> {
                setError(state.boardError, tilOnBoard)
                setError(state.densityError, tilDensity)
                setError(state.requiredError, tilReq)
            }

            is RefuelUIState.Result -> {
                if (state.success) {
                    state.data?.let { showData(it) }
                } else {
                    toastError(state.error)
                }
            }
        }
    }

    private fun setError(errorModel: Pair<Int, Int?>?, inputLayout: TextInputLayout) {
        errorModel?.let { error ->
            val (intRes, unit) = error
            if (unit != null) {
                inputLayout.error = getString(intRes, getString(unit))
            } else {
                inputLayout.error = getString(intRes)
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
        tvRT.text =
            String.format("%s:\n%s", getString(R.string.right_fuel_tank), refuelResult.right)
        tvCT.text =
            String.format("%s:\n%s", getString(R.string.center_fuel_tank), refuelResult.centre)
        TextViewCompat.setTextAppearance(
            tvCT,
            if (refuelResult.centreOver) R.style.TankInfoError else R.style.TankInfoDefault
        )
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

      fun setOstatMassUnit(unitRes: Int) {
        binding.tvOstatMass.text = getText(unitRes)
    }

      fun setReqMassUnit(unitRes: Int) {
        binding.tilReq.hint = getString(R.string.required_kilo, getString(unitRes))
    }

      fun setOstatVolumeUnit(unitRes: Int) {
        binding.tvReqHeader.text = getString(unitRes)
    }





    private fun toastError(wrappedString: IWrappedString?) {
        wrappedString?.toString(requireContext())?.let {
            toastError(requireContext(), it)
        }
    }

    private fun setEdtMassUnit(unitRes: Int?) {
        if (unitRes != null) {
            binding.tilOnBoard.hint = getString(R.string.has_on_board, getString(unitRes))
        }
    }

    private fun setEdtVolumeUnit(unitRes: Int?) {
        if (unitRes != null) {
            binding.tvOstatMass.hint = getString(R.string.total_volume_litre, getString(unitRes))
        }
    }

    private fun toastSuccess(wrappedString: IWrappedString?) {
        wrappedString?.toString(requireContext())?.let {
            toastSuccess(requireContext(), it)
        }
    }

    private fun setBtnDelVisible(visible: Boolean) {
        binding.btnRemoveData.isVisible = visible
    }

    private fun setBtnSaveVisible(visible: Boolean) {
        binding.btnSaveToFile.isVisible = visible
    }
}
