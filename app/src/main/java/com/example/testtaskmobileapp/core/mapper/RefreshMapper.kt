package com.example.testtaskmobileapp.core.mapper

import com.example.testtaskmobileapp.core.domain.models.RefreshTokenResponse
import com.example.testtaskmobileapp.core.dto.RefreshResponseTokenDto

class RefreshMapper: Mapper<RefreshResponseTokenDto, RefreshTokenResponse> {
    override fun toDomain(dto: RefreshResponseTokenDto): RefreshTokenResponse =
        RefreshTokenResponse(
            accessToken = dto.accessToken,
            refreshToken = dto.refreshToken
        )

}