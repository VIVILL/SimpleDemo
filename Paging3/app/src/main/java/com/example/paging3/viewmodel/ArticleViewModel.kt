package com.example.paging3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paging3.data.Article
import com.example.paging3.data.ArticleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ArticleViewModel(
    repository: ArticleRepository,
) : ViewModel(){
    /**
     * Stream of [Article]s for the UI.
     */
    // recyclerview 显示的数据
    val items: StateFlow<List<Article>> = repository.articleStream
            //通过 stateIn , flow 转 StateFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )
}