package com.example.testtaskmobileapp.core.api

import com.example.testtaskmobileapp.core.dto.RefreshRequestTokenDto
import com.example.testtaskmobileapp.core.dto.RefreshResponseTokenDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiRefreshService {
    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshToken(@Body request: RefreshRequestTokenDto?): Response<RefreshResponseTokenDto>
}