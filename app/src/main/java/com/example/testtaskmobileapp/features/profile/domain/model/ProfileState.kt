package com.example.testtaskmobileapp.features.profile.domain.model

data class ProfileState(
    val isLoading: Boolean = true,
    val avatarUri: String? = "",
    val avatarUrl: String? = "",
    val phone: String = "",
    val username: String = "",
    val city: String? = "Nothing",
    val birthday: String? = "Nothing",
    val zodiacSign: String? = "Nothing",
    val aboutMe: String? = "Nothing",
    val showToast: Boolean = false,
    val errorMessage: Int = 0,
    val showDatePicker: Boolean = false,
    val selectedBirthday: String = "",
    val fileName: String = "",
    val base64: String = ""
)