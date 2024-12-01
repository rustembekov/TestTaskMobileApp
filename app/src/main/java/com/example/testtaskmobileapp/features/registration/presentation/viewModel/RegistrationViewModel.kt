package com.example.testtaskmobileapp.features.registration.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.testtaskmobileapp.NavigationItem
import com.example.testtaskmobileapp.features.registration.data.repository.ApiRegisterRepository
import com.example.testtaskmobileapp.features.registration.presentation.controller.RegistrationController
import com.example.testtaskmobileapp.features.registration.domain.model.RegistrationState
import com.example.testtaskmobileapp.core.domain.models.StatusEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val apiRegisterRepository: ApiRegisterRepository
) : ViewModel(), RegistrationController {
    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    private fun isValidUsername(username: String): Boolean {
        val usernameRegex = "^[A-Za-z0-9_-]+\$".toRegex()
        return usernameRegex.matches(username)
    }

    private fun getErrorMessageFromBody(errorBody: String?): String {
        return try {
            val jsonObject = errorBody?.let { JSONObject(it) }
            val detail = jsonObject?.optJSONObject("detail")
            detail?.optString("message") ?: "Unknown error"
        } catch (e: JSONException) {
            "Unknown error"
        }
    }


    override fun updateName(name: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    name = name
                )
            }
        }
    }

    override fun updateSurname(surname: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    surname = surname
                )
            }
        }
    }

    override fun updateUsername(username: String) {
        viewModelScope.launch {
            val isError = !isValidUsername(username)

            _state.update {
                it.copy(
                    username = username,
                    showToast = isError

                )
            }
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

    override fun saveUser(phoneNumber: String, navController: NavController) {
        viewModelScope.launch {
            try {
                apiRegisterRepository.registerUser(
                    phone = phoneNumber,
                    username = _state.value.username,
                    name = _state.value.name
                )

                navController.navigate(
                    NavigationItem.StatusScreen.createRoute(
                        messageId = StatusEnum.REGISTERED.messageResId.toString(),
                        isSuccess = true,
                        phoneNumber = phoneNumber
                    )
                )
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val message = getErrorMessageFromBody(errorBody)

                    navController.navigate(
                        NavigationItem.StatusScreen.createRoute(
                            message = message,
                            isSuccess = false,
                            phoneNumber = phoneNumber
                        )
                    )
                } else {
                    navController.navigate(
                        NavigationItem.StatusScreen.createRoute(
                            messageId = StatusEnum.FAILURE_REGISTER.messageResId.toString(),
                            isSuccess = false,
                            phoneNumber = phoneNumber
                        )
                    )
                }
            } catch (e: Exception) {
                navController.navigate(
                    NavigationItem.StatusScreen.createRoute(
                        messageId = StatusEnum.FAILURE_REGISTER.messageResId.toString(),
                        isSuccess = false,
                        phoneNumber = phoneNumber
                    )
                )
            }
        }
    }

}