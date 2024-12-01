package com.example.testtaskmobileapp.core.utilities

import com.example.testtaskmobileapp.core.domain.RefreshResponseRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val refreshResponseRepository: RefreshResponseRepository,
    private val preferenceManager: PreferenceManager
) {
    private val mutex = Mutex()

    fun getAccessToken(): String? {
        return preferenceManager.accessToken
    }

    suspend fun refreshAccessToken(): String? {
        return mutex.withLock {
            val refreshToken = preferenceManager.refreshToken
            if (refreshToken.isNullOrEmpty()) {
                preferenceManager.clear()
                return null
            }

            return try {
                val response = refreshResponseRepository.refreshNewAccessToken(refreshToken)
                val newAccessToken = response?.accessToken
                val newRefreshToken = response?.refreshToken
                if (newRefreshToken != null && newAccessToken != null) {
                    preferenceManager.saveTokens(newRefreshToken, newAccessToken)
                }
                newAccessToken
            } catch (e: Exception) {
                e.printStackTrace()
                preferenceManager.clear()
                null
            }
        }
    }
}

