package com.example.testtaskmobileapp.core.utilities

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    context: Context
) {
    private val sharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    var accessToken: String? by StringPreference(sharedPreferences, "access_token", null)
    var refreshToken: String? by StringPreference(sharedPreferences, "refresh_token", null)
    var userId: Int? by IntPreference(sharedPreferences, "user_id", null)

    var username: String? by StringPreference(sharedPreferences, "username", null)
    var phone: String? by StringPreference(sharedPreferences, "phone", null)
    var city: String? by StringPreference(sharedPreferences, "city", null)
    var birthday: String? by StringPreference(sharedPreferences, "birthday", null)
    var zodiacSign: String? by StringPreference(sharedPreferences, "zodiac_sign", null)
    var aboutMe: String? by StringPreference(sharedPreferences, "about_me", null)
    var avatar: String? by StringPreference(sharedPreferences, "avatar", null)
    var vk: String? by StringPreference(sharedPreferences, "vk", null)
    var instagram: String? by StringPreference(sharedPreferences, "instagram", null)
    var status: String? by StringPreference(sharedPreferences, "status", null)


    fun saveTokens(refreshToken: String, accessToken: String, userId: Int? = null) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.userId = userId
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}



