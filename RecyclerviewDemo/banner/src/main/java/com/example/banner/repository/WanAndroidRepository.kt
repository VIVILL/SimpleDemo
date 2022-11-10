package com.example.banner.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.banner.api.WanAndroidApi
import com.example.banner.bean.Article
import com.example.banner.bean.Banner
import com.example.banner.bean.BannerResponse
import com.example.banner.paging.HomeArticlePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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