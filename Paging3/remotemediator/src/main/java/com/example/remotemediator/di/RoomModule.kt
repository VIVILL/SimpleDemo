package com.example.remotemediator.di

import android.app.Application
import com.example.remotemediator.db.RepoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {
    @Singleton
    @Provides
    fun provideRepoDatabase(application: Application): RepoDatabase {
        return RepoDatabase.getInstance(application)
    }
}