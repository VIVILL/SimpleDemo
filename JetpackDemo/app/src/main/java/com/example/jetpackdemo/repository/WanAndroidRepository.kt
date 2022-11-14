package com.example.jetpackdemo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.jetpackdemo.api.WanAndroidApi
import com.example.jetpackdemo.bean.Article
import com.example.jetpackdemo.bean.Banner
import com.example.jetpackdemo.paging.HomeArticlePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WanAndroidRepository @Inject constructor(
    private val wanAndroidApi: WanAndroidApi
){
    companion object {
        private const val HOME_ARTICLE_PAGE_SIZE = 20
    }

    fun getHomePageArticle(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                pageSize = HOME_ARTICLE_PAGE_SIZE
            ),
            pagingSourceFactory = { HomeArticlePagingSource(wanAndroidApi) }
        ).flow
    }

    fun getBannerFlow(): Flow<List<Banner>> {
        return flow {
            wanAndroidApi.getBanner().data?.let {
                emit(it)
            }
        }
    }

}