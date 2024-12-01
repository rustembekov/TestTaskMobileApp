package com.example.testtaskmobileapp.core.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.testtaskmobileapp.core.api.ApiConst
import com.example.testtaskmobileapp.core.api.ApiRefreshService
import com.example.testtaskmobileapp.core.domain.RefreshResponseRepository
import com.example.testtaskmobileapp.core.mapper.RefreshMapper
import com.example.testtaskmobileapp.core.utilities.PreferenceManager
import com.example.testtaskmobileapp.features.auth.data.api.ApiAuthService
import com.example.testtaskmobileapp.features.registration.data.api.ApiRegisterService
import com.example.testtaskmobileapp.core.utilities.TokenManager
import com.example.testtaskmobileapp.features.profile.data.api.ApiProfileService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager {
        return PreferenceManager(context)
    }

    @Provides
    @Singleton
    fun provideRefreshResponseRepository(
        apiRefreshService: ApiRefreshService,
        refreshMapper: RefreshMapper,
        tokenManagerProvider: dagger.Lazy<TokenManager>
    ): RefreshResponseRepository {
        return RefreshResponseRepository(apiRefreshService, refreshMapper, tokenManagerProvider)
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        refreshResponseRepository: RefreshResponseRepository,
        preferenceManager: PreferenceManager
    ): TokenManager {
        return TokenManager(refreshResponseRepository, preferenceManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        tokenManagerProvider: dagger.Lazy<TokenManager>
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(context))
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val accessToken = tokenManagerProvider.get().getAccessToken()
                if (!accessToken.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $accessToken")
                }
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConst.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiAuthService(retrofit: Retrofit): ApiAuthService {
        return retrofit.create(ApiAuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRegisterService(retrofit: Retrofit): ApiRegisterService {
        return retrofit.create(ApiRegisterService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRefreshService(retrofit: Retrofit): ApiRefreshService {
        return retrofit.create(ApiRefreshService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiProfileService(retrofit: Retrofit): ApiProfileService {
        return retrofit.create(ApiProfileService::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}