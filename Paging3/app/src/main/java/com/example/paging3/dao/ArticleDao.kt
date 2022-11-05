package com.example.paging3.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.paging3.data.Article

@Dao
interface ArticleDao {

    /**
     * Room knows how to return a LivePagedListProvider, from which we can get a LiveData and serve
     * it back to UI via ViewModel.
     */
    @Query("SELECT * FROM Article ORDER BY id COLLATE NOCASE ASC")
    fun getAllArticleById(): PagingSource<Int, Article>

    @Insert
    suspend fun addAllList(articleList: List<Article>)

    @Delete
    suspend fun deleteArticle(article: Article):Int

}