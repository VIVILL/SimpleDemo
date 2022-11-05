package com.example.paging3.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.paging3.data.Article
import com.example.paging3.data.ArticleRepository
import com.example.paging3.data.firstArticleCreatedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val ITEMS_PER_PAGE = 50
class ArticleViewModel(
    private val repository: ArticleRepository,
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

    val items: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            enablePlaceholders = false),
        // 使用 自定义 PagingSource
        pagingSourceFactory = { repository.articlePagingSource() }
    )
        .flow
        .cachedIn(viewModelScope)


    val allArticlePagingData: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(
            pageSize = 60,
            enablePlaceholders = true,
            maxSize = 200
        ),
        // 通过 room 获取 PagingSource
        pagingSourceFactory = { repository.getAllArticleById() }
    )
        .flow
        .cachedIn(viewModelScope)


    private val articleList = (0..500).map {
        Article(
            //id = it,
            id = 0,
            title = "Article $it",
            description = "This describes article $it",
            // minusDays(int n) 生成当前日期之前 n 天的日期
            created = firstArticleCreatedTime.minusDays(it.toLong())
        )
    }

    fun addLocalList() {
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                //处理耗时操作
                repository.addAllList(
                    articleList
                )
            }
        }
    }


    fun delete(article: Article) {
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                //处理耗时操作
                repository.delete(article)
            }
        }

    }
}