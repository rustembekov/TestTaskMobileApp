package com.example.testtaskmobileapp.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class UserProfileEditDto(
    @SerializedName("name")
    val name: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("birthday")
    val birthday: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("vk")
    val vk: String,

    @SerializedName("instagram")
    val instagram: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("avatar")
    val avatar: AvatarDto
)

data class AvatarDto(
    @SerializedName("filename")
    val filename: String,

    @SerializedName("base_64")
    val base64: String
)