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

private const val TAG = "BannerViewModel"

@HiltViewModel
class BannerViewModel @Inject constructor(
    private val repository: WanAndroidRepository
) : ViewModel(){

    private val _bannerListLiveData =  MutableLiveData<List<Banner>>()
    val bannerListLiveData: LiveData<List<Banner>> = _bannerListLiveData

    private val bannerList  = mutableListOf<Banner>()

    fun updateBannerList(){
        Log.d(TAG,"inner updateBannerList")
        viewModelScope.launch {
            try {
                bannerList.clear()
                bannerList.addAll(repository.getBanner().data!!)
                _bannerListLiveData.postValue(bannerList)
                Log.d(TAG,"bannerListSize = ${bannerList.size}")
            } catch(e: Exception) {
                e.printStackTrace()
            }

        }
    }

    // 获取文章
    fun getArticle(): Flow<PagingData<Article>> {
        return repository.getHomePageArticle().cachedIn(viewModelScope)
    }
}