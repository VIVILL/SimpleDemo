package com.example.jetpackdemo.bean

import com.google.gson.annotations.SerializedName


data class Banner(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("imagePath") val imagePath: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("url") val url: String
)
