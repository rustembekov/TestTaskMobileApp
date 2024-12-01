package com.example.testtaskmobileapp.features.registration.presentation.controller

import androidx.navigation.NavController

interface RegistrationController {
    fun updateName(name: String)
    fun updateSurname(surname: String)
    fun updateUsername(username: String)
    fun updateShowToast()
    fun saveUser(phoneNumber: String, navController: NavController)
}