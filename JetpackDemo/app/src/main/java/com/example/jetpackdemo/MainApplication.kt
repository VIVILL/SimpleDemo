package com.example.jetpackdemo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext

    }

    companion object {
        lateinit var instance: Application

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}