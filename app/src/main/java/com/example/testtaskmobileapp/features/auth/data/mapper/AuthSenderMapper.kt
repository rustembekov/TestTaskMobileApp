package com.example.testtaskmobileapp.features.auth.data.mapper

import com.example.testtaskmobileapp.features.auth.data.dto.SendAuthResponseDto
import com.example.testtaskmobileapp.core.mapper.Mapper
import com.example.testtaskmobileapp.features.auth.domain.model.AuthSenderResponse

class AuthSenderMapper : Mapper<SendAuthResponseDto, AuthSenderResponse> {
    override fun toDomain(dto: SendAuthResponseDto): AuthSenderResponse =
        AuthSenderResponse(
            isSuccess = dto.isSuccess
        )
}
