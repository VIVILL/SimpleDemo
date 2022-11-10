package com.example.banner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banner.adapter.HeaderAdapter
import com.example.banner.adapter.ArticleAdapter
import com.example.banner.adapter.BannerAdapter
import com.example.banner.bean.Banner
import com.example.banner.databinding.ActivityMainBinding
import com.example.banner.viewmodel.WanAndroidViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: WanAndroidViewModel by viewModels()

    private val bannerAdapter = BannerAdapter(ArrayList<Banner>())
    private val headerAdapter = HeaderAdapter(bannerAdapter)
    private val articleAdapter = ArticleAdapter()
    private val concatAdapter = ConcatAdapter(headerAdapter,articleAdapter)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        startObserver()

        binding.recyclerview.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerview.context,
                (binding.recyclerview.layoutManager as LinearLayoutManager).orientation
            )
        )
        // 为RecyclerView配置adapter
        binding.recyclerview.adapter = concatAdapter
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"inner onResume")
    }

    private fun startObserver() {
        // bannerListFlow
        lifecycleScope.launch {
            // We repeat on the STARTED lifecycle because an Activity may be PAUSED
            // but still visible on the screen, for example in a multi window app
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bannerListFlow.collect{ value ->
                    // 更新 list
                    bannerAdapter.bannerList = value
                    Log.d(TAG," bannerList size = ${bannerAdapter.bannerList.size}")
                    //数据改变刷新视图
                    bannerAdapter.notifyDataSetChanged()

                }
            }
        }

        // 设置 banner 无限循环滑动
        // 使用 launchWhenResumed 能保证 使得息屏后 不执行定时任务
        lifecycleScope.launchWhenResumed {  //onPause 的时候会暂停.
            Log.d(TAG,"inner launchWhenResumed")
            var counter = 0
            repeat(Int.MAX_VALUE){
                delay(3000L)
                if (bannerAdapter.bannerList.size > 0){
                    val position = ++counter % bannerAdapter.bannerList.size
                    Log.d(TAG,"currentIndex = $counter bannerListSize = ${bannerAdapter.bannerList.size} position = $position")
                    headerAdapter.smoothScrollToPosition(position)
                }

            }
        }


        lifecycleScope.launch {
            // We repeat on the STARTED lifecycle because an Activity may be PAUSED
            // but still visible on the screen, for example in a multi window app
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 获取 PagingData
                viewModel.getArticle()
                    .catch {
                        Log.d(TAG,"Exception : ${it.message}")
                    }
                    .collectLatest {
                    // paging 使用 submitData 填充 adapter
                    articleAdapter.submitData(it)
                }
            }
        }

    }

}