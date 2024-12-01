package com.example.testtaskmobileapp.features.profile.presentation.controller

import androidx.navigation.NavController

interface ProfileController {
    fun onEditClick(navController: NavController)
    fun onExitClick(navController: NavController)
}