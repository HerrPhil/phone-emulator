package com.phone.emulator.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PhoneInfoViewModel : ViewModel() {

    private val TAG: String = "The phone info view model"

    private val _phoneInfoUiState = MutableStateFlow(
        PhoneInfoUiState()
    )

    val phoneInfoUiState: StateFlow<PhoneInfoUiState> = _phoneInfoUiState

    init {

        Log.i(TAG, "initialize any values of phone info")

        modifyPhoneInfoValue("")
    }

    fun onPhoneInfoChanged(selectedNumber: String) {

        Log.i(TAG, "on phone info changed")

        modifyPhoneInfoValue(selectedNumber)

    }

    fun onDial(callAction: (String) -> Unit) {
        Log.i(TAG, "TODO integrate a telephony repository to delegate to context (activity) using application/app container pattern")
        callAction(_phoneInfoUiState.value.number)
    }

    private fun modifyPhoneInfoValue(value: String) {
        _phoneInfoUiState.update {
            it.copy(
                number = value
            )
        }
    }

}