package com.example.testtaskmobileapp.core.domain

import com.example.testtaskmobileapp.core.api.ApiRefreshService
import com.example.testtaskmobileapp.core.domain.models.RefreshTokenResponse
import com.example.testtaskmobileapp.core.dto.RefreshRequestTokenDto
import com.example.testtaskmobileapp.core.mapper.RefreshMapper
import com.example.testtaskmobileapp.core.utilities.TokenManager
import retrofit2.Response
import javax.inject.Inject

class RefreshResponseRepository @Inject constructor(
    private val apiRefreshService: ApiRefreshService,
    private val refreshMapper: RefreshMapper,
    private val tokenManagerProvider: dagger.Lazy<TokenManager>
) {

    private val tokenManager: TokenManager
        get() = tokenManagerProvider.get()

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): T? {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()
            } else if (response.code() == 401) {
                val newToken = tokenManager.refreshAccessToken()
                if (!newToken.isNullOrEmpty()) {
                    apiCall().body()
                } else {
                    throw Exception("Token refresh failed")
                }
            } else {
                throw Exception("API error: ${response.message()}")
            }
        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
            null
        }
    }

    suspend fun refreshNewAccessToken(refreshToken: String): RefreshTokenResponse? {
        return safeApiCall {
            val request = RefreshRequestTokenDto(refreshToken = refreshToken)
            apiRefreshService.refreshToken(request)
        }?.let { dto -> refreshMapper.toDomain(dto) }
    }
}

