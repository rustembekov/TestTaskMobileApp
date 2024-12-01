package com.example.testtaskmobileapp.core.domain.models

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)