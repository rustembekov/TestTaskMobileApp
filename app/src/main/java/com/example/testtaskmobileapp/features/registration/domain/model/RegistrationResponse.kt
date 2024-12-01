package com.example.testtaskmobileapp.features.registration.domain.model

data class RegistrationResponse(
    val id: Int,
    val refreshToken: String,
    val accessToken: String,
    val isExistingUser: Boolean? = null
)