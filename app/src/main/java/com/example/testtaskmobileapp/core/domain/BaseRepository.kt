package com.example.testtaskmobileapp.core.domain

import com.example.testtaskmobileapp.core.utilities.TokenManager
import retrofit2.Response

abstract class BaseRepository(
    private val tokenManager: TokenManager
) {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): T? {
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
}
