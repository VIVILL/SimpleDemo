package com.example.paging3

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.paging3.data.ArticleRepository
import com.example.paging3.viewmodel.ViewModelFactory

object Injection {
    // 使用 单例 创建 repository
    private fun provideArticleRepository(): ArticleRepository = ArticleRepository()

    fun provideViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideArticleRepository())
    }
}