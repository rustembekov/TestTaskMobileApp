package com.example.testtaskmobileapp.features.profile.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.testtaskmobileapp.NavigationItem
import com.example.testtaskmobileapp.core.api.ApiConst
import com.example.testtaskmobileapp.core.utilities.PreferenceManager
import com.example.testtaskmobileapp.features.profile.data.repository.ApiUserProfileRepository
import com.example.testtaskmobileapp.features.profile.domain.model.ProfileState
import com.example.testtaskmobileapp.features.profile.presentation.controller.ProfileController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoField
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiUserProfileRepository: ApiUserProfileRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel(), ProfileController {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                throwable.message
            }
        ) {
            val savedData = listOf(
                preferenceManager.username,
                preferenceManager.phone,
                preferenceManager.userId
            )

            if (savedData.all { it != null }) {
                val birthday = preferenceManager.birthday.orEmpty()

                _state.update {
                    it.copy(
                        avatarUrl = preferenceManager.avatar.orEmpty(),
                        phone = preferenceManager.phone.orEmpty(),
                        username = preferenceManager.username.orEmpty(),
                        city = preferenceManager.city.orEmpty(),
                        birthday = birthday,
                        zodiacSign = if (birthday.isNotEmpty()) calculateZodiacSign(birthday) else "",
                        aboutMe = preferenceManager.aboutMe.orEmpty(),
                        selectedBirthday = birthday,
                        isLoading = false
                    )
                }
            } else {
                val userProfileResponse = apiUserProfileRepository.getUserProfile()
                userProfileResponse?.let { userProfile ->
                    val birthday = userProfile.birthday.orEmpty()

                    preferenceManager.apply {
                        username = userProfile.username
                        phone = userProfile.phone
                        city = userProfile.city
                        this.birthday = birthday
                        avatar = ApiConst.BASE_API_URL + userProfile.avatarUrl
                        vk = userProfile.vk
                        instagram = userProfile.instagram
                        status = userProfile.status
                        zodiacSign = if (birthday.isNotEmpty()) calculateZodiacSign(birthday) else null
                    }

                    _state.update { currentState ->
                        currentState.copy(
                            avatarUrl = ApiConst.BASE_API_URL + userProfile.avatarUrl.orEmpty(),
                            phone = userProfile.phone,
                            username = userProfile.username,
                            city = userProfile.city.orEmpty(),
                            birthday = birthday,
                            zodiacSign = if (birthday.isNotEmpty()) calculateZodiacSign(birthday) else "",
                            aboutMe = userProfile.instagram.orEmpty(),
                            selectedBirthday = birthday,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun calculateZodiacSign(birthday: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(birthday, formatter)
            val dayOfYear = date.get(ChronoField.DAY_OF_YEAR)

            when (dayOfYear) {
                in 20..49 -> "Aquarius" // Водолей
                in 50..79 -> "Pisces" // Рыбы
                in 80..109 -> "Aries" // Овен
                in 110..140 -> "Taurus" // Телец
                in 141..171 -> "Gemini" // Близнецы
                in 172..202 -> "Cancer" // Рак
                in 203..233 -> "Leo" // Лев
                in 234..264 -> "Virgo" // Дева
                in 265..295 -> "Libra" // Весы
                in 296..325 -> "Scorpio" // Скорпион
                in 326..355 -> "Sagittarius" // Стрелец
                else -> "Capricorn" // Козерог
            }
        } catch (e: Exception) {
            "Unknown" // Fallback in case of invalid date
        }
    }

    override fun onEditClick(navController: NavController) {
       navController.navigate(NavigationItem.ProfileEdit.route)
    }

    override fun onExitClick(navController: NavController) {
        preferenceManager.clear()
        navController.navigate(NavigationItem.Auth.route) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

}