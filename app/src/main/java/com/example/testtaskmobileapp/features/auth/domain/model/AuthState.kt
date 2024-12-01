package com.example.testtaskmobileapp.features.auth.domain.model;

data class AuthState(
    val isLoading: Boolean = false,
    val selectedPhoneNumber: String = "",
    val selectedCountryNumberCode: String = "",
    val errorMessage: String? = null,
    val verificationCode: String = "",
    val isPhoneValid: Boolean = false,
    val isCodeValid: Boolean = false,
    val isSuccess: Boolean = false,
    val showToast: Boolean = false,
    val digits: List<String> = List(6) { "" },
    val detectedCountryCode: String = ""
)
