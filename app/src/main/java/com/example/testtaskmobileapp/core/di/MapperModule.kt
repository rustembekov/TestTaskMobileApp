package com.example.testtaskmobileapp.core.di

import com.example.testtaskmobileapp.core.mapper.RefreshMapper
import com.example.testtaskmobileapp.features.auth.data.mapper.AuthCheckerMapper
import com.example.testtaskmobileapp.features.auth.data.mapper.AuthSenderMapper
import com.example.testtaskmobileapp.features.profile.data.mapper.UserProfileMapper
import com.example.testtaskmobileapp.features.profile.data.mapper.UserProfileUpdateMapper
import com.example.testtaskmobileapp.features.registration.data.mapper.UserRegisterMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    @Singleton
    fun provideUserRegisterMapper(): UserRegisterMapper {
        return UserRegisterMapper()
    }

    @Provides
    @Singleton
    fun provideUserProfileUpdateMapper(): UserProfileUpdateMapper {
        return UserProfileUpdateMapper()
    }


    @Provides
    @Singleton
    fun provideUserProfileMapper(): UserProfileMapper {
        return UserProfileMapper()
    }

    @Provides
    @Singleton
    fun provideRefreshMapper(): RefreshMapper {
        return RefreshMapper()
    }

    @Provides
    @Singleton
    fun provideAuthSenderMapper(): AuthSenderMapper {
        return AuthSenderMapper()
    }

    @Provides
    @Singleton
    fun provideAuthCheckerMapper(): AuthCheckerMapper {
        return AuthCheckerMapper()
    }
}
