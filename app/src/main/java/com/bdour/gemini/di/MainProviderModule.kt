package com.bdour.gemini.di

import com.bdour.gemini.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainProviderModule {

    private const val MODEL_GEMINI_PRO = "gemini-pro"

    @Singleton
    @Provides
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = MODEL_GEMINI_PRO,
            apiKey = BuildConfig.API_KEY
        )
    }

}