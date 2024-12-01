package com.example.testtaskmobileapp.features.registration.data.api

import com.example.testtaskmobileapp.features.registration.data.dto.RegisterRequestDto
import com.example.testtaskmobileapp.features.registration.data.dto.RegisterResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiRegisterService {

    @POST("/api/v1/users/register/")
    suspend fun registerUser(
        @Body request: RegisterRequestDto
    ): RegisterResponseDto
}

