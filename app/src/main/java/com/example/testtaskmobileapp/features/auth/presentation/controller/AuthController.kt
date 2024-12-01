package com.example.testtaskmobileapp.features.auth.presentation.controller

import android.content.Context
import androidx.navigation.NavController

interface AuthController {
    fun sendPhoneNumber(navController: NavController)
    fun updateCountryCode(selectedCountyCode: String)
    fun updatePhoneNumber(selectedPhoneNumber: String)
    fun updateOtpValueChange(value: String, navController: NavController, phone: String)
    fun updateShowToast()
    fun detectCountryNumberCode(context: Context)
}