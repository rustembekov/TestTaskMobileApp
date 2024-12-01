package com.example.testtaskmobileapp.features.auth.data.dto

import com.google.gson.annotations.SerializedName

data class CheckAuthResponseDto(
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("is_user_exists") val isUserExist: Boolean,
)