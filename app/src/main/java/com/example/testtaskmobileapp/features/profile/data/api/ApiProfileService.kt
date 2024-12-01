package com.example.testtaskmobileapp.features.profile.data.api

import com.example.testtaskmobileapp.features.profile.data.dto.UserProfileDto
import com.example.testtaskmobileapp.features.profile.data.dto.UserProfileEditDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ApiProfileService {

    @GET("/api/v1/users/me/")
    suspend fun getUser(): UserProfileDto

    @PUT("/api/v1/users/me/")
    suspend fun updateUserProfile(
        @Body request: UserProfileEditDto
    ): Response<Unit>
}
