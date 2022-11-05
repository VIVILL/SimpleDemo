package com.example.paging3.data

import com.example.paging3.paging.ArticlePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

private val firstArticleCreatedTime = LocalDateTime.now()

// 定义 Repository 类
class ArticleRepository {

    // 创建 flow 数据流
    val articleStream: Flow<List<Article>> = flowOf(
        (0..500).map {
            Article(
                id = it,
                title = "Article $it",
                description = "This describes article $it",
                // minusDays(int n) 生成当前日期之前 n 天的日期
                created = firstArticleCreatedTime.minusDays(it.toLong())
            )
    })

    // 使用 PagingSource
    fun articlePagingSource() = ArticlePagingSource()
}
