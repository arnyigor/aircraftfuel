package com.arny.aircraftrefueling.presentation.deicing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.data.utils.launchWhenCreated
import com.arny.aircraftrefueling.data.utils.strings.IWrappedString
import com.arny.aircraftrefueling.data.utils.strings.ResourceString
import com.arny.aircraftrefueling.databinding.FragmentDeicingBinding
import com.arny.aircraftrefueling.di.viewModelFactory
import com.arny.aircraftrefueling.utils.KeyboardHelper.hideKeyboard
import com.arny.aircraftrefueling.utils.ToastMaker
import com.arny.aircraftrefueling.utils.alertDialog
import com.arny.aircraftrefueling.utils.shareFile
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.AndroidSupportInjection
import dagger.assisted.AssistedFactory
import java.io.File
import javax.inject.Inject

class DeicingFragment : Fragment() {

    private lateinit var binding: FragmentDeicingBinding

    @AssistedFactory
    internal interface ViewModelFactory {
        fun create(): DeicingViewModel
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: DeicingViewModel by viewModelFactory { viewModelFactory.create() }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

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
        initListeners()
        observeData()
        viewModel.initVM()
    }

    private fun observeData() {
        launchWhenCreated { viewModel.deicingUIState.collect(::updateState) }
        launchWhenCreated { viewModel.toastError.collect(::toastError) }
        launchWhenCreated { viewModel.toastSuccess.collect(::toastSuccess) }
        launchWhenCreated { viewModel.btnDelVisible.collect(::setBtnDelVisible) }
        launchWhenCreated { viewModel.btnSaveVisible.collect(::setBtnSaveVisible) }
        launchWhenCreated { viewModel.edtMassUnit.collect(::setEdtMassUnit) }
        launchWhenCreated { viewModel.edtVolumeUnit.collect(::setEdtVolumeUnit) }
        launchWhenCreated { viewModel.hideKeyboard.collect { hideKeyboard(requireActivity()) } }
        launchWhenCreated { viewModel.shareFilePath.collect(::onShareFile) }
    }

    private fun updateState(state: DeicingUIState) = with(binding) {
        when (state) {
            DeicingUIState.IDLE -> {}
            is DeicingUIState.CheckPVK -> {
                tilPercent.isVisible = state.checked
                editPercentPVK.isEnabled = state.checked
                editPercentPVK.setText(state.percentPvk)
            }

            is DeicingUIState.InputError -> {
                setError(state.boardError, tilTotalVolume)
                setError(state.densityError, tilDensity)
                setError(state.percentError, tilPercent)
            }

            is DeicingUIState.Result -> {
                if (state.success) {
                    binding.editTotalMassFuel.setText(state.data)
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

    private fun initListeners() {
        with(binding) {
            buttonLitreCnt.setOnClickListener {
                viewModel.onLitreCountClick(
                    tiedtTotalVolume.text.toString(),
                    editDensityPVK.text.toString(),
                    editPercentPVK.text.toString()
                )
            }
            checkPVK.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onCheckPvk(isChecked)
            }
            tiedtTotalVolume.doAfterTextChanged { tilTotalVolume.error = null }
            editDensityPVK.doAfterTextChanged { tilDensity.error = null }
            editPercentPVK.doAfterTextChanged { tilPercent.error = null }
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
            btnShareData.setOnClickListener { viewModel.onShareFileClick() }
        }
    }

    private fun toastError(wrappedString: IWrappedString?) {
        wrappedString?.toString(requireContext())?.let {
            ToastMaker.toastError(requireContext(), it)
        }
    }

    private fun setEdtMassUnit(unitRes: Int?) {
        if (unitRes != null) {
            binding.tilTotalMass.hint = getString(R.string.total_mass_kilo, getString(unitRes))
        }
    }

    private fun setEdtVolumeUnit(unitRes: Int?) {
        if (unitRes != null) {
            binding.tilTotalVolume.hint = getString(R.string.total_volume_litre, getString(unitRes))
        }
    }

    private fun toastSuccess(wrappedString: IWrappedString?) {
        wrappedString?.toString(requireContext())?.let {
            ToastMaker.toastSuccess(requireContext(), it)
        }
    }

    private fun setBtnDelVisible(visible: Boolean) {
        binding.btnRemoveData.isVisible = visible
        binding.btnShareData.isVisible = visible
    }

    private fun setBtnSaveVisible(visible: Boolean) {
        binding.btnSaveToFile.isVisible = visible
    }

    private fun onShareFile(filePath: String?) {
        if (filePath != null) {
            if (filePath.isNotBlank()) {
                shareFile(File(filePath))
            } else {
                toastError(ResourceString(R.string.error_file_not_found))
            }
        }
    }
}
