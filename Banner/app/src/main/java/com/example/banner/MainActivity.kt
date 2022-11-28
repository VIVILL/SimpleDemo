package com.example.banner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.banner.adapter.BodyAdapter
import com.example.banner.adapter.HeaderAdapter
import com.example.banner.adapter.HeaderItemAdapter
import com.example.banner.databinding.ActivityMainBinding
import com.example.banner.viewmodel.AutoScrollAction
import com.example.banner.viewmodel.BannerViewModel
import com.example.banner.viewmodel.TouchAction
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var job: Job
    private val viewModel: BannerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val urlData = listOf(
            //最后一张图片
            "http://t8.baidu.com/it/u=198337120,441348595&fm=79&app=86&f=JPEG?w=1280&h=732",

            "http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&f=JPEG?w=1280&h=853",
            "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/a6efce1b9d16fdfabf36882ab08f8c5495ee7b9f.jpg",
            "http://t8.baidu.com/it/u=198337120,441348595&fm=79&app=86&f=JPEG?w=1280&h=732",

            //第一张图片
            "http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&f=JPEG?w=1280&h=853"
        )

        val headerItemAdapter = HeaderItemAdapter(urlData){
            Log.d(TAG,"onclick it = $it")
        }
        val headerAdapter = HeaderAdapter(headerItemAdapter)
        headerAdapter.setOnViewAttachedListener {
            Log.d(TAG, "viewModel.autoScroll()")
            viewModel.autoScroll()
        }
        headerAdapter.setOnViewDetachedListener {
            Log.d(TAG, "viewModel.cancelAutoScroll()")
            viewModel.cancelAutoScroll()
        }
        headerAdapter.setTouchEventListener { motionEvent ->
            Log.d(TAG, "inner touch motionEvent = $motionEvent")
            viewModel.touch(motionEvent)
        }

        val bodyStringList = listOf("aaa","bbb","ccc","aaa","bbb","ccc"
            ,"aaa","bbb","ccc","aaa","bbb","ccc","aaa","bbb","ccc"
            ,"aaa","bbb","ccc","aaa","bbb","ccc","aaa","bbb","ccc")

        val bodyAdapter = BodyAdapter(bodyStringList)

        val concatAdapter =  ConcatAdapter(headerAdapter,bodyAdapter)

        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.recyclerView.adapter = concatAdapter

        // 设置需要缓存的 ViewHolder数量 防止离屏后再显示时 频繁执行 onBindViewHolder
        binding.recyclerView.setItemViewCacheSize(10)

        lifecycleScope.launch(exceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.autoScrollAction.collect {
                    when (it) {
                        is AutoScrollAction.AutoScroll -> {
                            Log.d(TAG, "inner AutoScroll")
                            // 通过设置动画，实现 自动滑动时 平滑滚动
                            headerAdapter.setCurrentItem()
                            Log.d(TAG, "after setCurrentItem position duration 300")

                        }
                    }

                }

            }
        }

        lifecycleScope.launch(exceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.touchAction.collect {
                    when (it) {
                        is TouchAction.Touch -> {
                            Log.d(TAG, "inner Touch")
                            when (it.event.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    Log.d(TAG, "inner MotionEvent.ACTION_DOWN")
                                    viewModel.isAutoScroll = false
                                }
                                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL-> {
                                    Log.d(TAG, "inner MotionEvent.ACTION_UP or ACTION_CANCEL")
                                    // 先取消之前的任务
                                    if (::job.isInitialized) {
                                        job.cancel()
                                        Log.d(TAG, "after job.cancel()")
                                    }
                                    Log.d(TAG, "after set autoScroll = false")
                                    job = lifecycleScope.launch {
                                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                                            Log.d(TAG, "after launch")
                                            // 等待5s后 重新开始 无限循环
                                            delay(5000L)
                                            viewModel.isAutoScroll = true
                                            Log.d(TAG, "after delay 5000L, after set autoScroll = true")

                                        }
                                    }
                                }

                                else -> {}
                            }
                        }

                    }
                }
            }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.d(TAG, "CoroutineExceptionHandler exception : ${exception.message}")
    }
}