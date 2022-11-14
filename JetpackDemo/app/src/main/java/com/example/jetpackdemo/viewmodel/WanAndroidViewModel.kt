package com.example.jetpackdemo.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.jetpackdemo.bean.Article
import com.example.jetpackdemo.bean.Banner
import com.example.jetpackdemo.repository.WanAndroidRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "WanAndroidViewModel"

@HiltViewModel
class WanAndroidViewModel @Inject constructor(
    private val repository: WanAndroidRepository
) : ViewModel(){
    private val _bannerListStateFlow = MutableStateFlow<List<Banner>>(ArrayList())

    val bannerListStateFlow: StateFlow<List<Banner>> = _bannerListStateFlow

    fun updateBannerList() {
        // 更新  value 数据
        viewModelScope.launch {
            _bannerListStateFlow.value = repository.getBannerFlow()
                .catch { throwable ->
                    // Catch exceptions in all down stream flow
                    // Any error occurs after this catch operator
                    // will not be caught here
                    println(throwable)
                }
                .stateIn(viewModelScope)
                .value
        }
    }

    // 获取文章
    fun getArticle(): Flow<PagingData<Article>> {
        return repository.getHomePageArticle().cachedIn(viewModelScope)
    }
}