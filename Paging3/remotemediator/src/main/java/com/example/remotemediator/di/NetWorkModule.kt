package com.example.remotemediator.di

import com.example.remotemediator.api.GithubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetWorkModule {

    @Singleton
    @Provides
    fun provideGithubApi(): GithubApi {
        return GithubApi.create()
    }
}