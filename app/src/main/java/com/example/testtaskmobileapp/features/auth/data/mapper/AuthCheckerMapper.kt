package com.example.testtaskmobileapp.features.auth.data.mapper

import com.example.testtaskmobileapp.core.mapper.Mapper
import com.example.testtaskmobileapp.features.auth.data.dto.CheckAuthResponseDto
import com.example.testtaskmobileapp.features.auth.domain.model.AuthCheckerResponse

class AuthCheckerMapper : Mapper<CheckAuthResponseDto, AuthCheckerResponse> {
    override fun toDomain(dto: CheckAuthResponseDto): AuthCheckerResponse =
        AuthCheckerResponse(
            userId = dto.userId,
            accessToken = dto.accessToken,
            refreshToken = dto.refreshToken,
            isUserExist = dto.isUserExist
        )

}