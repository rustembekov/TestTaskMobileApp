package com.example.testtaskmobileapp.features.profile.data.repository

import com.example.testtaskmobileapp.core.domain.BaseRepository
import com.example.testtaskmobileapp.core.utilities.TokenManager
import com.example.testtaskmobileapp.features.profile.data.api.ApiProfileService
import com.example.testtaskmobileapp.features.profile.data.mapper.UserProfileMapper
import com.example.testtaskmobileapp.features.profile.data.mapper.UserProfileUpdateMapper
import com.example.testtaskmobileapp.features.profile.domain.model.UserProfile
import com.example.testtaskmobileapp.features.profile.domain.model.UserUpdateProfile
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiUserProfileRepository @Inject constructor(
    private val apiProfileService: ApiProfileService,
    private val userProfileMapper: UserProfileMapper,
    private val userProfileUpdateMapper: UserProfileUpdateMapper,
    tokenManager: TokenManager
) : BaseRepository(tokenManager) {

    suspend fun getUserProfile(): UserProfile? {
        return try {
            safeApiCall {
                val responseDto = apiProfileService.getUser()
                val userProfile = userProfileMapper.toDomain(responseDto)
                Response.success(userProfile)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserProfile(userProfile: UserUpdateProfile): Boolean {
        return try {
            safeApiCall {
                val requestDto = userProfileUpdateMapper.toDomain(userProfile)
                val response = apiProfileService.updateUserProfile(requestDto)

                Response.success(response.isSuccessful)
            } ?: false
        } catch (e: Exception) {
            false
        }
    }


}