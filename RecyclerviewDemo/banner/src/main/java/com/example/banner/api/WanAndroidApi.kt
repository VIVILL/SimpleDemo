package com.example.banner.api

import com.example.banner.bean.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

//api 接口说明 https://www.wanandroid.com/blog/show/2
interface WanAndroidApi {

    companion object {
        private const val BASE_URL = "https://www.wanandroid.com/"

        fun create(): WanAndroidApi {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WanAndroidApi::class.java)
        }
    }



    // https://www.wanandroid.com/article/list/0/json
    /**
     * 获取文章
     */
    @GET("/article/list/{page}/json")
    suspend fun getHomePageArticle(
        @Path("page") page: Int
    ): WanAndroidResponse<Articles<Article>>


    // https://www.wanandroid.com/banner/json
    /**
     * Banner
     */
    @GET("banner/json")
    suspend fun getBanner(): BannerResponse<List<Banner>>
}