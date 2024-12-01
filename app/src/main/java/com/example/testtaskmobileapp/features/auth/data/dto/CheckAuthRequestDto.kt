package com.example.testtaskmobileapp.features.auth.data.dto

import com.google.gson.annotations.SerializedName

data class CheckAuthRequestDto(
    @SerializedName("phone") val phone: String,
    @SerializedName("code") val code: String
)
