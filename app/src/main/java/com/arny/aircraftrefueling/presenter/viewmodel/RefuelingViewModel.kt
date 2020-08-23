package com.arny.aircraftrefueling.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arny.aircraftrefueling.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RefuelingViewModel : ViewModel() {
    private val _uiState = MutableLiveData<RerfuelUIState>()
    private val tankRefueler = TankRefueler()
    val uiState: LiveData<RerfuelUIState> = _uiState

    fun refuel(density: String, onBoard: String, required: String, volumeUnitType: Int) {
        viewModelScope.launch {
            try {
                val refueler = withContext(Dispatchers.Default) {
                    tankRefueler.vUnit = volumeUnitType
                    tankRefueler.calculate(required.toDouble(), density.toDouble(), onBoard.toDouble())
                    tankRefueler
                }
                _uiState.value = RerfuelUIState.ResultState(Result.Success(refueler))
            } catch (e: Exception) {
                _uiState.value = RerfuelUIState.ResultState(Result.Error(e))
            }
        }

        /*        try {
                mRo = desnsity.toDouble()
                mMassOstat = edtTotalFuel.getText().toString().toFloat().toDouble()
                mMassRequired = edtRequiredfuel.getText().toString().toFloat().toDouble()
                val totStr: String = LitreCnt(mMassOstat, mRo, mMassRequired)
                var totLit: Double = if (totStr != "0" || totStr != "") totStr.toDouble() else 0
                if (totLit < 0) {
                    Local.toast(context, getString(R.string.error_wrong_data), false)
                    return
                }
                totLit = getTotLit(totLit)
                tvTotalLitre.text = String.format(Locale.getDefault(), "%.0f", totLit)
                tvTotalKilo.text = String.format(Locale.getDefault(), "%.0f", mReqMassTotal)
                tankFueling(mMassRequired, mRo)
                tvLT.text = String.format("%s:\n%s", getString(R.string.left_fuel_tank), left)
                tvRT.text = String.format("%s:\n%s", getString(R.string.right_fuel_tank), right)
                tvCT.text = String.format("%s:\n%s", getString(R.string.center_fuel_tank), centre)
                btnKiloFileSave.setVisibility(View.VISIBLE)
            } catch (e: Exception) {
                e.printStackTrace()
            }*/
    }
}