package com.example.testtaskmobileapp.features.auth.domain.model

data class AuthCheckerResponse(
    val refreshToken: String?,
    val accessToken: String?,
    val userId: Int?,
    val isUserExist: Boolean,
)