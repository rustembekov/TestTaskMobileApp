package com.example.testtaskmobileapp.features.profile.data.mapper

import com.example.testtaskmobileapp.core.mapper.Mapper
import com.example.testtaskmobileapp.features.profile.data.dto.UserProfileDto
import com.example.testtaskmobileapp.features.profile.domain.model.UserProfile

class UserProfileMapper : Mapper<UserProfileDto, UserProfile> {
    override fun toDomain(dto: UserProfileDto): UserProfile {
        val profileData = dto.profileData

        return UserProfile(
            id = profileData.id.onMappingException(fieldName = "id", dto = profileData),
            name = profileData.name.onMappingException(fieldName = "name", dto = profileData),
            username = profileData.username.onMappingException(fieldName =  "username", dto = profileData),
            phone = profileData.phone.onMappingException(fieldName = "phone", dto = profileData),
            birthday = profileData.birthday,
            city = profileData.city,
            vk = profileData.vk,
            instagram = profileData.instagram,
            status = profileData.status,
            avatarUrl = profileData.avatars?.avatar,
            bigAvatarUrl = profileData.avatars?.bigAvatar,
            miniAvatarUrl = profileData.avatars?.miniAvatar,
            online = profileData.online.onMappingException(fieldName = "online", dto = profileData),
            last = profileData.last,
            createdDate = profileData.created.onMappingException(fieldName = "created", dto = profileData),
            completedTaskCount = profileData.completedTask
        )
    }


    private fun <T : Any, Y> T?.onMappingException(fieldName: String, dto: Y): T {
        return this ?: throw Exception("Error on field: $fieldName. DTO: $dto")
    }

}