package com.example.remotemediator.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.remotemediator.bean.Repo
import com.example.remotemediator.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(
    private val repository: GithubRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    // 获取 Repo
    fun getRepo(): Flow<PagingData<Repo>> {
        return repository.getSearchResultStream("Android").cachedIn(viewModelScope)
    }
}



