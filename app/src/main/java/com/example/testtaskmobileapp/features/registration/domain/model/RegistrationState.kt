package com.example.testtaskmobileapp.features.registration.domain.model

data class RegistrationState(
    val isLoading: Boolean = false,
    val name: String = "",
    val surname: String = "",
    val username: String = "",
    val showToast: Boolean = false

)