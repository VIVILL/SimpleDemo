package com.example.banner.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.banner.databinding.ItemParentHeaderBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "HeaderAdapter"
class HeaderAdapter(private val adapter: HeaderItemAdapter) :
    RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemParentHeaderBinding) : RecyclerView.ViewHolder(binding.root), LifecycleOwner{
        var counter = 0
        lateinit var job: Job
        private var autoScroll : Boolean = true

        init {
            itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                // View onDetached 的时候回调 onDestroy()
                override fun onViewDetachedFromWindow(v: View?) {
                    Log.d(TAG,"inner onViewDetachedFromWindow")
                  //  itemView.removeOnAttachStateChangeListener(this)
                    // 在Lifecycle被销毁之后，协程会跟着取消
                    onDestroy()
                }

                // View onAttached 的时候回调 onCreate()
                override fun onViewAttachedToWindow(v: View?) {
                    Log.d(TAG,"inner onViewAttachedToWindow")
                    onStart()
                }
            })
        }

        lateinit var lifecycleRegistry: LifecycleRegistry

        fun onStart() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
            Log.d(TAG,"inner onStart currentState = ${lifecycleRegistry.currentState}")
        }


        fun onDestroy() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        private lateinit var headerItemAdapter:HeaderItemAdapter
        fun bind(adapter: HeaderItemAdapter) {
            Log.d(TAG,"inner bind")
            // view holder 离开屏幕后再次出现在屏幕上时 counter 清0 如需保存离屏前数据可使用viewmodel
            counter = 0
            // 默认 自动滑动
            autoScroll = true

            Log.d(TAG,"currentItem = ${binding.viewPager.currentItem} , after set autoScroll = true")

            headerItemAdapter = adapter

            binding.viewPager.adapter = adapter
            // 设置离屏加载 防止滑动时白屏
            binding.viewPager.offscreenPageLimit = adapter.itemCount
            //  circleIndicator 设置 adapter
            binding.circleIndicator.setViewPager(binding.viewPager)

            lifecycleRegistry = LifecycleRegistry(this)
            Log.d(TAG,"after lifecycleRegistry")

            // 添加监听
            binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    Log.d(TAG, "onPageScrolled: position = $position, positionOffset = $positionOffset, positionOffsetPixels = $positionOffsetPixels"
                    )
                }

                override fun onPageSelected(position: Int) {
                    Log.d(TAG, "onPageSelected: position = $position")
                }

                override fun onPageScrollStateChanged(state: Int) {
                    Log.d(TAG, "onPageScrollStateChanged: state = $state")
                    onChanged(state)
                }
            })

            lifecycleScope.launch {
                Log.d(TAG,"inner launch")
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    Log.d(TAG, "inner repeatOnLifecycle")

                    repeat(Int.MAX_VALUE) {
                        Log.d(TAG, "inner repeat")
                        delay(3000L)
                        Log.d(TAG, "after delay 3000L autoScroll = $autoScroll counter = $counter")
                        if (headerItemAdapter.dataList.isNotEmpty() && autoScroll) {
                            Log.d(TAG, "counter = $counter")
                            val position = ++counter % headerItemAdapter.dataList.size
                            // 设置smoothScroll = false，关闭过渡动画
                            Log.d(TAG, "before setCurrentItem position = $position")
                            binding.viewPager.setCurrentItem(position, false)
                        }
                    }
                }
            }
        }


        // 人工滑动 时暂停自动滑动
        private fun onChanged(state: Int) {
            when (state) {
                //滑动结束
                ViewPager.SCROLL_STATE_IDLE -> {
                    Log.d(TAG,"inner SCROLL_STATE_IDLE")
                }
                ViewPager.SCROLL_STATE_DRAGGING -> {
                    Log.d(TAG,"inner SCROLL_STATE_DRAGGING")
                    // 人工滑动时 会走到这里
                    // 先取消之前的任务
                    if (this::job.isInitialized){
                        job.cancel()
                        Log.d(TAG,"after job.cancel()")
                    }
                    autoScroll = false
                    Log.d(TAG,"after set autoScroll = false")
                    job = lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            Log.d(TAG,"after launch")
                            // 等待5s后 重新开始 无限循环
                            delay(5000L)
                            // 保存当前位置
                            counter = binding.viewPager.currentItem
                            autoScroll = true
                            Log.d(TAG,"after delay 5000L counter = $counter , after set autoScroll = true")

                        }
                    }
                }

                else -> {}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemParentHeaderBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(adapter)
    }

    override fun getItemCount(): Int = 1
}