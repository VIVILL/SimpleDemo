package com.example.banner.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.banner.bean.Article
import com.example.banner.bean.Banner
import com.example.banner.repository.WanAndroidRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "WanAndroidViewModel"

@HiltViewModel
class WanAndroidViewModel @Inject constructor(
    private val repository: WanAndroidRepository
) : ViewModel(){
    private val _bannerListFlow: Flow<List<Banner>> =
        repository.getBannerFlow()
            .catch { throwable ->
                // Catch exceptions in all down stream flow
                // Any error occurs after this catch operator
                // will not be caught here
                println(throwable)
            }

    val bannerListFlow: Flow<List<Banner>> = _bannerListFlow

    // 获取文章
    fun getArticle(): Flow<PagingData<Article>> {
        return repository.getHomePageArticle().cachedIn(viewModelScope)
    }
}