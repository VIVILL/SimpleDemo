package com.example.banner.di

import com.example.banner.api.WanAndroidApi
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
    fun provideWanAndroidApi(): WanAndroidApi {
        return WanAndroidApi.create()
    }
}