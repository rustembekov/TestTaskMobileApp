@file:Suppress("PLUGIN_IS_NOT_ENABLED")

package com.example.testtaskmobileapp.features.registration.data.dto;

import com.google.gson.annotations.SerializedName

data class RegisterResponseDto(
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("user_id") val userId: Int
)


