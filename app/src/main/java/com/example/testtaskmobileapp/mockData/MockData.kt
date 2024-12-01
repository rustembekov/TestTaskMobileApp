package com.example.testtaskmobileapp.mockData

import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.features.auth.domain.model.AuthState
import com.example.testtaskmobileapp.features.chats.model.ChatState
import com.example.testtaskmobileapp.features.profile.domain.model.ProfileState
import com.example.testtaskmobileapp.features.registration.domain.model.RegistrationState

object MockData {
    val mockAuthState = AuthState(
        isLoading = true,
        selectedPhoneNumber = "+79123456789",
        selectedCountryNumberCode = "7",
        verificationCode = "2121",
        isPhoneValid = false,
        isCodeValid = false,
        isSuccess = false,
        digits =  List(6) { "33433"}

    )

    val mockRegistrationState = RegistrationState(
        isLoading = false,
        name = "Sabyrzhan",
        surname = "Rustembekov",
        username = "test123",
    )

    val mockProfileState = ProfileState(
        isLoading = false,
        avatarUri = "https://example.com/avatar.jpg",
        phone = "+1234567890",
        username = "JohnDoe",
        city = "Almaty",
        birthday = "1990-01-01",
        zodiacSign = "Capricorn",
        aboutMe = "A passionate software developer who loves coding and exploring new technologies.",
        showToast = true,
        errorMessage = R.string.successfully_edited
    )


    val mockChatState = ChatState(
        isLoading = false
    )
}