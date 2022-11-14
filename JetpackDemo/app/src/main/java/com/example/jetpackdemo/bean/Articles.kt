package com.example.jetpackdemo.bean

import com.google.gson.annotations.SerializedName


data class Articles<T>(
    @field:SerializedName("curPage") val curPage: Int,
    @field:SerializedName("datas") val datas: List<T>,
    @field:SerializedName("offset") val offset: Int,
    @field:SerializedName("pageCount") val pageCount: Int,
    @field:SerializedName("size") val size: Int,
    @field:SerializedName("total") val total: Int
)