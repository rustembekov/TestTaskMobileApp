package com.example.testtaskmobileapp.features.registration.data.mapper

import com.example.testtaskmobileapp.core.mapper.Mapper
import com.example.testtaskmobileapp.features.registration.domain.model.RegistrationResponse
import com.example.testtaskmobileapp.features.registration.data.dto.RegisterResponseDto

class UserRegisterMapper : Mapper<RegisterResponseDto, RegistrationResponse> {
    override fun toDomain(dto: RegisterResponseDto): RegistrationResponse {
        return RegistrationResponse(
            id = dto.userId,
            refreshToken = dto.refreshToken,
            accessToken = dto.accessToken,
        )
    }
}
