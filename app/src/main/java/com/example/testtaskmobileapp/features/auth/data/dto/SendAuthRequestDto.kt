package com.example.testtaskmobileapp.features.auth.data.dto

import com.google.gson.annotations.SerializedName

data class SendAuthRequestDto(
    @SerializedName("phone") val phone: String
)
