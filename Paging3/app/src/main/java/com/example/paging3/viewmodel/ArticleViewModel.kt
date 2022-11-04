package com.example.paging3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paging3.data.Article
import com.example.paging3.data.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val ITEMS_PER_PAGE = 50
class ArticleViewModel(
    repository: ArticleRepository,
) : ViewModel(){
    /**
     * Stream of [Article]s for the UI.
     */
    // recyclerview 显示的数据
/*    val items: StateFlow<List<Article>> = repository.articleStream
            //通过 stateIn , flow 转 StateFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf()
        )*/

    // 通过 paging 显示数据
    val items: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            enablePlaceholders = false),
        pagingSourceFactory = { repository.articlePagingSource() }
    )
        .flow
        .cachedIn(viewModelScope)
}