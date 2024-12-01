package com.example.testtaskmobileapp.core.dto

import com.google.gson.annotations.SerializedName

data class RefreshRequestTokenDto(
    @SerializedName("refresh_token") val refreshToken: String
)