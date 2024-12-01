package com.example.testtaskmobileapp.features.auth.data.api

import com.example.testtaskmobileapp.features.auth.data.dto.CheckAuthRequestDto
import com.example.testtaskmobileapp.features.auth.data.dto.CheckAuthResponseDto
import com.example.testtaskmobileapp.features.auth.data.dto.SendAuthRequestDto
import com.example.testtaskmobileapp.features.auth.data.dto.SendAuthResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAuthService {
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body request: SendAuthRequestDto): SendAuthResponseDto

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: CheckAuthRequestDto): Response<CheckAuthResponseDto>
}