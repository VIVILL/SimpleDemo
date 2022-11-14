package com.example.jetpackdemo.bean

import com.google.gson.annotations.SerializedName

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */

data class BannerResponse<T>(
    @field:SerializedName("data") val data: T?,
    @field:SerializedName("errorCode") val errorCode: Int,
    @field:SerializedName("errorMsg") val errorMsg: String
)
