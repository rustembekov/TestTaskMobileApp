package com.example.testtaskmobileapp.features.registration.data.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("phone") val phone: String,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String
)

