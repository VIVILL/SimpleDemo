package com.example.jetpackdemo.bean

data class WanAndroidResponse<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)
