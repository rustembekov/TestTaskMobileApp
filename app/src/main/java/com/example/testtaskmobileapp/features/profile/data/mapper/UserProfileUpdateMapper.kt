package com.example.testtaskmobileapp.features.profile.data.mapper

import com.example.testtaskmobileapp.core.mapper.Mapper
import com.example.testtaskmobileapp.features.profile.data.dto.AvatarDto
import com.example.testtaskmobileapp.features.profile.data.dto.UserProfileEditDto
import com.example.testtaskmobileapp.features.profile.domain.model.UserUpdateProfile

class UserProfileUpdateMapper : Mapper<UserUpdateProfile, UserProfileEditDto> {
    override fun toDomain(domain: UserUpdateProfile): UserProfileEditDto {
        return UserProfileEditDto(
            name = domain.name,
            username = domain.username,
            birthday = domain.birthday,
            city = domain.city,
            vk = domain.vk,
            instagram = domain.instagram,
            status = domain.status,
            avatar = AvatarDto(
                filename = domain.avatar.filename,
                base64 = domain.avatar.base64
            )
        )
    }
}