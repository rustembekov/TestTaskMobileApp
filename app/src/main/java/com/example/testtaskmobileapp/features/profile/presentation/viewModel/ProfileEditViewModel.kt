package com.example.testtaskmobileapp.features.profile.presentation.viewModel

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.testtaskmobileapp.NavigationItem
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.core.utilities.PreferenceManager
import com.example.testtaskmobileapp.features.profile.data.repository.ApiUserProfileRepository
import com.example.testtaskmobileapp.features.profile.domain.model.AvatarDomain
import com.example.testtaskmobileapp.features.profile.domain.model.ProfileState
import com.example.testtaskmobileapp.features.profile.domain.model.UserUpdateProfile
import com.example.testtaskmobileapp.features.profile.presentation.controller.ProfileEditController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val apiUserProfileRepository: ApiUserProfileRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel(), ProfileEditController {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun getFileNameAndBase64(uri: Uri, contentResolver: ContentResolver): Pair<String, String> {
        val fileName = getFileNameFromUri(uri, contentResolver)

        val base64 = getBase64FromUri(uri, contentResolver)

        return Pair(fileName, base64)
    }

    private fun getFileNameFromUri(uri: Uri, contentResolver: ContentResolver): String {
        var cursor: Cursor? = null
        var fileName = ""

        try {
            cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow("_display_name")
                fileName = cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }

        return fileName
    }

    private fun getBase64FromUri(uri: Uri, contentResolver: ContentResolver): String {
        var inputStream: InputStream? = null
        var base64 = ""

        try {
            inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val byteArray = inputStream.readBytes()
                base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        return base64
    }

    private fun loadUserProfile() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                throwable.message
            }
        ) {

            _state.update {
                it.copy(
                    avatarUrl = preferenceManager.avatar.orEmpty(),
                    phone = preferenceManager.phone.orEmpty(),
                    username = preferenceManager.username.orEmpty(),
                    city = preferenceManager.city.orEmpty(),
                    birthday = preferenceManager.birthday,
                    zodiacSign = preferenceManager.zodiacSign,
                    aboutMe = preferenceManager.aboutMe.orEmpty(),
                    selectedBirthday = preferenceManager.birthday.orEmpty(),
                    isLoading = false
                )
            }
        }
    }


    override fun onUsernameChange(newUsername: String) {
        _state.update {
            it.copy(
                username = newUsername
            )
        }
    }

    override fun onPhoneChange(newPhone: String) {
        _state.update {
            it.copy(
                phone = newPhone
            )
        }
    }

    override fun onCityChange(newCity: String) {
        _state.update {
            it.copy(
                city = newCity
            )
        }
    }

    override fun onBirthdayChange(newBirthday: String) {
        _state.update {
            it.copy(
                birthday = newBirthday,
                selectedBirthday = newBirthday,
                showDatePicker = false
            )
        }
    }

    override fun onZodiacSignChange(newZodiacSign: String) {
        _state.update {
            it.copy(
                zodiacSign = newZodiacSign
            )
        }
    }

    override fun onAboutMeChange(newAboutMe: String) {
        _state.update {
            it.copy(
                aboutMe = newAboutMe
            )
        }
    }

    override fun updateShowToast() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    showToast = false
                )
            }
        }
    }

    override fun updateShowDatePicker(showDate: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    showDatePicker = showDate
                )
            }
        }
    }

    override fun onChangeAvatar(uri: Uri, contentResolver: ContentResolver) {
        val (fileName, base64) = getFileNameAndBase64(uri, contentResolver)

        viewModelScope.launch {
            _state.update {
                it.copy(
                    avatarUri = uri.toString(),
                    fileName = fileName,
                    base64 = base64
                )
            }

        }
    }


    override fun onSaveClick(navController: NavController) {
        viewModelScope.launch {
            val userProfile = UserUpdateProfile(
                name = _state.value.username,
                username = _state.value.username ,
                birthday = _state.value.birthday ?: "",
                city = _state.value.city ?: "",
                vk = preferenceManager.vk ?: "",
                instagram = preferenceManager.instagram ?: "",
                status = preferenceManager.status ?: "",
                avatar = AvatarDomain(
                    filename = _state.value.fileName,
                    base64 = _state.value.base64
                )
            )
            val response = apiUserProfileRepository.updateUserProfile(userProfile = userProfile)
            if (response) {
                _state.update {
                    it.copy(
                        showToast = true,
                        errorMessage = R.string.successfully_edited
                    )
                }
                preferenceManager.birthday = _state.value.birthday
                preferenceManager.city = _state.value.city
                preferenceManager.avatar = _state.value.avatarUri

                delay(1000)
                navController.navigate(NavigationItem.Profile.route)
            } else {
                _state.update {
                    it.copy(
                        showToast = true,
                        errorMessage = R.string.please_check_fields
                    )
                }
            }
        }
    }


    override fun onCancelClick(navController: NavController) {
        navController.popBackStack()
    }
}