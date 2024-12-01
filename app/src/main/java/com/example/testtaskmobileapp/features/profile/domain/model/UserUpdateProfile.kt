package com.example.testtaskmobileapp.features.profile.domain.model

data class UserUpdateProfile(
    val name: String,
    val username: String,
    val birthday: String,
    val city: String,
    val vk: String,
    val instagram: String,
    val status: String,
    val avatar: AvatarDomain
)

data class AvatarDomain(
    val filename: String,
    val base64: String
)
