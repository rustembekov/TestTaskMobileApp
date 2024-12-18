package com.example.testtaskmobileapp.core.dto

import com.google.gson.annotations.SerializedName

data class RefreshResponseTokenDto(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)