package com.example.testtaskmobileapp.features.auth.data.dto

import com.google.gson.annotations.SerializedName

data class SendAuthResponseDto(
    @SerializedName("is_success") val isSuccess: Boolean
)