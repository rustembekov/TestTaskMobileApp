package com.example.testtaskmobileapp.features.profile.presentation.controller

import android.content.ContentResolver
import android.net.Uri
import androidx.navigation.NavController

interface ProfileEditController {
    fun onChangeAvatar(uri: Uri, contentResolver: ContentResolver)
    fun onSaveClick(navController: NavController)
    fun onCancelClick(navController: NavController)
    fun onUsernameChange(newUsername: String)
    fun onPhoneChange(newPhone: String)
    fun onCityChange(newCity: String)
    fun onBirthdayChange(newBirthday: String)
    fun onZodiacSignChange(newZodiacSign: String)
    fun onAboutMeChange(newAboutMe: String)
    fun updateShowToast()
    fun updateShowDatePicker(showDate: Boolean)
}
