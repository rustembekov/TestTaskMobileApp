package com.example.testtaskmobileapp.features.auth.presentation.viewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.testtaskmobileapp.NavigationItem
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.core.domain.models.StatusEnum
import com.example.testtaskmobileapp.features.auth.presentation.controller.AuthController
import com.example.testtaskmobileapp.features.auth.data.repository.ApiAuthRepository
import com.example.testtaskmobileapp.features.auth.domain.model.AuthState
import com.google.android.gms.location.LocationServices
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiAuthRepository: ApiAuthRepository
) : ViewModel(), AuthController {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private fun validatePhoneNumber(phone: String, countryCode: String): Boolean {
        val countryPhoneLength = mapOf(
            "1" to 10,  // US
            "44" to 10, // UK
            "7" to 10   // Russia, Kazakhstan
        )
        val phoneWithoutCode = phone.removePrefix(countryCode)

        val expectedLength = countryPhoneLength[countryCode]
        return expectedLength != null && phoneWithoutCode.length == expectedLength && phoneWithoutCode.all { it.isDigit() }
    }

    private fun checkOtpCode(navController: NavController, phone: String) {
        val currentOtpCode = _state.value.digits.joinToString("")

        viewModelScope.launch {
            try {
                val response = apiAuthRepository.checkAuthCode(
                    phone = phone,
                    code = currentOtpCode
                )
                println("Response boolean: $response")
                if (response) {
                    navController.navigate(
                        NavigationItem.StatusScreen.createRoute(
                            messageId = StatusEnum.LOGIN.messageResId.toString(),
                            isSuccess = true,
                            phoneNumber = phone
                        )
                    )
                } else {
                    navController.navigate(
                        NavigationItem.Registration.createRoute(phone = phone)
                    )
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
                navController.navigate(
                    NavigationItem.StatusScreen.createRoute(
                        messageId = StatusEnum.FAILURE_LOGIN.messageResId.toString(),
                        isSuccess = false,
                        phoneNumber = phone
                    )
                )
            }
        }
    }

    override fun sendPhoneNumber(navController: NavController) {
        val countryCode = state.value.selectedCountryNumberCode
        val phoneNumber = state.value.selectedPhoneNumber
        val fullPhoneNumber = "$countryCode$phoneNumber"

        val isPhoneValid = validatePhoneNumber(fullPhoneNumber, countryCode)

        if (isPhoneValid) {
            viewModelScope.launch {
                val isNumberValidOnServer = apiAuthRepository.sendPhoneNumber(fullPhoneNumber)
                _state.update {
                    it.copy(
                        isPhoneValid = isNumberValidOnServer,
                        showToast = isNumberValidOnServer,
                        errorMessage = if (!isNumberValidOnServer) R.string.invalid_phone_number.toString() else null
                    )
                }

                if (isNumberValidOnServer) {
                    navController.navigate(
                        NavigationItem.PhoneVerification.createRoute(
                            fullPhoneNumber
                        )
                    )
                }
            }
        } else {
            _state.update {
                it.copy(
                    showToast = true,
                    errorMessage = R.string.invalid_phone_format.toString()
                )
            }
        }
    }


    override fun updatePhoneNumber(selectedPhoneNumber: String) {
        val sanitizedInput = selectedPhoneNumber.filter { it.isDigit() }

        _state.update {
            it.copy(
                selectedPhoneNumber = sanitizedInput
            )
        }
    }

    override fun updateOtpValueChange(value: String, navController: NavController, phone: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    digits = value.padEnd(6, ' ').take(6).map { it.toString() }
                )
            }
            if (value.length == 6) {
                checkOtpCode(navController = navController, phone = phone)
            }
        }
    }


    override fun updateShowToast() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    showToast = false
                )
            }
        }
    }

    override fun updateCountryCode(selectedCountyCode: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    selectedCountryNumberCode = selectedCountyCode
                )
            }
        }
    }

    private fun updateCountryCode(countryCode: String, dialingCode: String) {
        viewModelScope.launch {
            _state.update { it.copy(detectedCountryCode = countryCode, selectedCountryNumberCode = dialingCode, isLoading = true) }
        }
    }

    override fun detectCountryNumberCode(context: Context) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses?.isNotEmpty() == true) {
                            val countryCode = addresses[0].countryCode
                            val phoneNumberUtil = PhoneNumberUtil.getInstance()
                            val countryDialingCode = phoneNumberUtil.getCountryCodeForRegion(countryCode)

                            updateCountryCode(countryCode, countryDialingCode.toString())
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("AuthController", "Error fetching location: ${exception.message}")
                }
        } else {
            Log.e("AuthController", "Location permissions not granted")
        }
    }

}