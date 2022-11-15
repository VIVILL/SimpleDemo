package com.example.remotemediator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remotemediator.adapter.ReposAdapter
import com.example.remotemediator.viewmodel.GithubViewModel
import com.example.remotemediator.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val githubViewModel: GithubViewModel by viewModels()

    private val repoAdapter = ReposAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        subscribeUI()

        binding.repoRecyclerview.addItemDecoration(
            DividerItemDecoration(
                binding.repoRecyclerview.context,
                (binding.repoRecyclerview.layoutManager as LinearLayoutManager).orientation
            )
        )
        // 为RecyclerView配置adapter
        binding.repoRecyclerview.adapter = repoAdapter

        binding.swipeLayout.setOnRefreshListener {
            // 更新 PagingDataAdapter
            repoAdapter.refresh()
        }
    }

    private fun subscribeUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 获取 PagingData
                githubViewModel.getRepo()
                    .catch {
                        Log.d(TAG,"Exception : ${it.message}")
                    }
                    .collectLatest {
                        Log.d(TAG,"inner collectLatest")
                        // paging 使用 submitData 填充 adapter
                        repoAdapter.submitData(it)
                    }
            }
        }

        //监听paging数据刷新状态
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repoAdapter.loadStateFlow.collectLatest { loadState ->
                    Log.d(TAG, "loadState  =  $loadState")
                    // 下拉刷新
                    binding.swipeLayout.isRefreshing = loadState.refresh is LoadState.Loading
                    // 上拉加载
                    binding.progressIndicator.isVisible = loadState.source.append is LoadState.Loading

                }
            }
        }
    }

}