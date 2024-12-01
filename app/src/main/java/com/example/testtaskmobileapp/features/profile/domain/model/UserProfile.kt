package com.example.testtaskmobileapp.features.profile.domain.model

import android.net.Uri

data class UserProfile(
    val id: Int,
    val name: String,
    val username: String,
    val phone: String,
    val birthday: String?,
    val city: String?,
    val vk: String?,
    val instagram: String?,
    val status: String?,
    val avatarUrl: String?,
    val bigAvatarUrl: String?,
    val miniAvatarUrl: String?,
    val online: Boolean,
    val last: String?,
    val createdDate: String,
    val completedTaskCount: Int?
)

//sealed class Avatar {
//    data class AvatarFromURL(val urls: String): Avatar()
//    data class AvatarFromURI(val uri: Uri): Avatar()
//}
//
//fun comompose(avatar: Avatar) {
//    when(avatar) {
//        is Avatar.AvatarFromURI -> {
//            //
//        }
//        is Avatar.AvatarFromURL -> {
//
//        }
//    }
//}