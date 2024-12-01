package com.example.testtaskmobileapp.features.auth.data.repository

import com.example.testtaskmobileapp.core.domain.BaseRepository
import com.example.testtaskmobileapp.core.utilities.PreferenceManager
import com.example.testtaskmobileapp.core.utilities.TokenManager
import com.example.testtaskmobileapp.features.auth.data.api.ApiAuthService
import com.example.testtaskmobileapp.features.auth.data.dto.CheckAuthRequestDto
import com.example.testtaskmobileapp.features.auth.data.dto.SendAuthRequestDto
import com.example.testtaskmobileapp.features.auth.data.mapper.AuthCheckerMapper
import com.example.testtaskmobileapp.features.auth.data.mapper.AuthSenderMapper
import retrofit2.Response
import javax.inject.Inject

class ApiAuthRepository @Inject constructor(
    private val apiAuthService: ApiAuthService,
    private val senderMapper: AuthSenderMapper,
    private val checkerMapper: AuthCheckerMapper,
    private val preferenceManager: PreferenceManager,
    tokenManager: TokenManager
) : BaseRepository(tokenManager) {

    suspend fun sendPhoneNumber(phone: String): Boolean {
        return safeApiCall {
            val requestDto = SendAuthRequestDto(phone)
            val responseDto = apiAuthService.sendAuthCode(requestDto)
            val authSenderResponse = senderMapper.toDomain(responseDto)
            Response.success(authSenderResponse.isSuccess)
        } ?: false
    }

    suspend fun checkAuthCode(phone: String, code: String): Boolean {
        return try {
            val requestDto = CheckAuthRequestDto(phone, code)
            val responseDto = safeApiCall { apiAuthService.checkAuthCode(requestDto) }
                ?: throw Exception("API call returned null response")

            val authCheckerResponse = checkerMapper.toDomain(responseDto)

            if (authCheckerResponse.isUserExist && !authCheckerResponse.refreshToken.isNullOrEmpty() && !authCheckerResponse.accessToken.isNullOrEmpty()) {
                preferenceManager.saveTokens(
                    refreshToken = authCheckerResponse.refreshToken,
                    accessToken = authCheckerResponse.accessToken,
                    userId = authCheckerResponse.userId
                )
            }

            authCheckerResponse.isUserExist
        } catch (e: Exception) {
            println("Error during API call: ${e.message}")
            throw e // Rethrow the exception to propagate it
        }
    }
}