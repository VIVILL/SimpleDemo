package com.example.paging3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paging3.data.Article
import com.example.paging3.databinding.ActivityMainBinding
import com.example.paging3.recyclerview.ArticleAdapter
import com.example.paging3.viewmodel.ArticleViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val articleViewModel by lazy {
        ViewModelProvider(this,Injection.provideViewModelFactory(
            owner = this,
            context = this)
        ).get(ArticleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        // 把数据加载到数据库
        articleViewModel.addLocalList()

        //val items = articleViewModel.items
        // 通过 room 获取 PagingData
        val items = articleViewModel.allArticlePagingData

        val articleAdapter = ArticleAdapter()

        articleAdapter.setOnItemClickListener {position,article ->
            Log.d(TAG,"position = $position article = $article")
            // 点击后 删除
            articleViewModel.delete(article)
        }
        // 给 recyclerview 绑定 adapter
        binding.bindAdapter(articleAdapter = articleAdapter)

        // Collect from the Article Flow in the ViewModel, and submit it to the
        // ListAdapter.
        lifecycleScope.launch {
            // We repeat on the STARTED lifecycle because an Activity may be PAUSED
            // but still visible on the screen, for example in a multi window app
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 调用 下游终止操作符 collect
/*                items.collect {
                    Log.d(TAG,"collect size = ${it.size}")
                    if (it.isNotEmpty()){
                        Log.d(TAG,"first = ${it[0]}")
                    }
                    articleAdapter.submitList(it)
                }*/
                items.collectLatest {
                    articleAdapter.submitData(it)
                }
            }
        }
        // 当 Paging 库提取更多项时，底部进度条会显示；当提取完成时，底部进度条会消失
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                articleAdapter.loadStateFlow.collect {
                    binding.prependProgress.isVisible = it.source.prepend is LoadState.Loading
                    binding.appendProgress.isVisible = it.source.append is LoadState.Loading
                }
            }
        }
    }

    private fun ActivityMainBinding.bindAdapter(articleAdapter: ArticleAdapter) {
        list.adapter = articleAdapter
        list.layoutManager = LinearLayoutManager(list.context)
        val decoration = DividerItemDecoration(list.context, DividerItemDecoration.VERTICAL)
        list.addItemDecoration(decoration)
    }
}