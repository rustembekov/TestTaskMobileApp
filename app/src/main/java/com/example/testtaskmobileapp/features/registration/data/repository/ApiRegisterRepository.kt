package com.example.testtaskmobileapp.features.registration.data.repository

import com.example.testtaskmobileapp.core.domain.BaseRepository
import com.example.testtaskmobileapp.core.utilities.PreferenceManager
import com.example.testtaskmobileapp.core.utilities.TokenManager
import com.example.testtaskmobileapp.features.registration.data.api.ApiRegisterService
import com.example.testtaskmobileapp.features.registration.data.dto.RegisterRequestDto
import com.example.testtaskmobileapp.features.registration.data.mapper.UserRegisterMapper
import com.example.testtaskmobileapp.features.registration.domain.model.RegistrationResponse
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRegisterRepository @Inject constructor(
    private val apiRegisterService: ApiRegisterService,
    private val userRegisterMapper: UserRegisterMapper,
    private val preferenceManager: PreferenceManager,
    tokenManager: TokenManager
) : BaseRepository(tokenManager) {

    suspend fun registerUser(phone: String, username: String, name: String): Response<RegistrationResponse?> {
        return try {
            val requestDto = RegisterRequestDto(phone, username, name)
            val responseDto = apiRegisterService.registerUser(requestDto)
            val resultRegisterResponse = userRegisterMapper.toDomain(responseDto)

            if (resultRegisterResponse.isExistingUser == true) {
                preferenceManager.saveTokens(
                    refreshToken = resultRegisterResponse.refreshToken,
                    accessToken = resultRegisterResponse.accessToken,
                    userId = resultRegisterResponse.id
                )
            }

            Response.success(resultRegisterResponse)
        } catch (e: Exception) {
            Response.error(500, "An error occurred: ${e.message}".toResponseBody())
        }
    }


}



