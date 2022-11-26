package com.example.banner

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.*
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.banner.adapter.HeaderItemAdapter
import com.example.banner.databinding.LayoutMyViewpager2Binding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "MyViewPager2"

class MyViewPager2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) , LifecycleOwner {
    private var binding: LayoutMyViewpager2Binding
    private lateinit var adapter: HeaderItemAdapter
    var currentPosition: Int = 0
    private var autoScroll : Boolean = true
    lateinit var job: Job
    private lateinit var lifecycleRegistry: LifecycleRegistry

    var adapterSize: Int = 0

    fun setAdapter(adapter: HeaderItemAdapter){
        Log.d(TAG,"inner setAdapter")
        this.adapter = adapter
        adapterSize = adapter.itemCount
        getViewPager2().adapter = adapter
        // 设置离屏加载 防止滑动时白屏
        getViewPager2().offscreenPageLimit = adapter.itemCount
    }

    fun setPosition(position: Int){
        Log.d(TAG,"inner setPosition position = $position")
        when (position) {
            in 1..adapterSize-2 -> {
                getViewPager2().setCurrentItem(position,false)
            }
            0 -> {
                getViewPager2().setCurrentItem(adapterSize-2,false)
            }
            adapterSize-1 -> {
                getViewPager2().setCurrentItem(1,false)
            }
        }
    }

    init {
        Log.d(TAG,"inner init")
        binding = LayoutMyViewpager2Binding.inflate(LayoutInflater.from(context),this,true)

        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            // View onDetached 的时候回调 onDestroy()
            override fun onViewDetachedFromWindow(v: View?) {
                Log.d(TAG,"inner onViewDetachedFromWindow")
                // 在Lifecycle被销毁之后，协程会跟着取消
                onDestroy()
                unRegister()
            }

            // View onAttached 的时候回调 onCreate()
            override fun onViewAttachedToWindow(v: View?) {
                Log.d(TAG,"inner onViewAttachedToWindow")
                onStart()
            }
        })
    }

    fun onStart() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        Log.d(TAG,"inner onStart currentState = ${lifecycleRegistry.currentState}")

        // 防止 手指滑动 后置 false 时 离屏导致没有执行置 true
        autoScroll = true
    }


    fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    private fun getViewPager2(): ViewPager2 {
        return binding.viewPager2
    }

    fun getViewPager2CurrentItem(): Int {
        return binding.viewPager2.currentItem
    }

    /**
     * 手指触摸时停止自动轮播,抬起手指后，5s后开启
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG,"inner dispatchTouchEvent ev = $ev")
        when (ev.action) {
            MotionEvent.ACTION_DOWN ->  {
                Log.d(TAG,"inner MotionEvent.ACTION_DOWN")
                autoScroll = false
            }
            MotionEvent.ACTION_UP ->  {
                Log.d(TAG,"inner MotionEvent.ACTION_UP")
                // 先取消之前的任务
                if (this::job.isInitialized){
                    job.cancel()
                    Log.d(TAG,"after job.cancel()")
                }
                Log.d(TAG,"after set autoScroll = false")
                job = lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        Log.d(TAG,"after launch")
                        // 等待5s后 重新开始 无限循环
                        delay(5000L)
                        // 保存当前位置
                        currentPosition = getViewPager2().currentItem
                        autoScroll = true
                        Log.d(TAG,"after delay 5000L currentPosition = $currentPosition , after set autoScroll = true")

                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 开启自动滚动
    fun autoScroll(){
        Log.d(TAG,"inner autoScroll")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repeat(Int.MAX_VALUE){
                    Log.d(TAG,"inner repeat")
                    delay(3000L)
                    Log.d(TAG,"after delay 3000L autoScroll = $autoScroll currentPosition = $currentPosition")
                    if (autoScroll){
                        Log.d(TAG,"currentPosition = $currentPosition")
                        val position = ++currentPosition
                        // setCurrentItem 后 onPageSelected 会被调用
                        //    binding.viewPager.setCurrentItem(position, false)
                        // 通过设置动画，实现 自动滑动时 平滑滚动
                        MyPagerHelper.setCurrentItem(getViewPager2(), position, 300)
                        Log.d(TAG,"after setCurrentItem position = $position , currentPosition = $currentPosition duration 300")
                    }
                }

            }
        }
    }

    // 创建圆点指示器
    fun createCircle(size: Int){
        binding.linearLayout.createCircle(/*adapter.itemCount - 3*/size)
        if(currentPosition == 1){// 1 显示的是第一张图片
            // 第一个圆 置 true 其余圆置 false
            binding.linearLayout.setSelected(0)
        }
    }

    private val onPageChangeCallback = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            //  Log.d(TAG, "onPageScrolled: position = $position, positionOffset = $positionOffset, positionOffsetPixels = $positionOffsetPixels")
        }

        override fun onPageSelected(position: Int) {
            Log.d(TAG, "inner onPageSelected: position = $position currentPosition = $currentPosition")
            currentPosition = position
            //position数据为   0 1 2 3 4
            // 对应的图片为图片  3 1 2 3 1

            //position数据为   0 1 2 3 4
            // 对应的圆点位置    2 0 1 2 0
            when (position) {
                in 1..adapterSize-2 -> {
                    binding.linearLayout.setSelected(position-1)
                }
                0 -> {
                    binding.linearLayout.setSelected(adapterSize-3)
                }
                adapterSize-1 -> {
                    binding.linearLayout.setSelected(0)
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            Log.d(TAG, "onPageScrollStateChanged: state = $state")
            onChanged(state)
        }
    }
    // 注册
    fun register(){
        lifecycleRegistry = LifecycleRegistry(this)
        Log.d(TAG,"after lifecycleRegistry")
        getViewPager2().registerOnPageChangeCallback(onPageChangeCallback)
    }

    fun unRegister(){
        Log.d(TAG,"inner unRegister")
        getViewPager2().unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    private fun onChanged(state: Int) {
        when (state) {
            //滑动结束
            ViewPager.SCROLL_STATE_IDLE -> {
                Log.d(TAG,"inner SCROLL_STATE_IDLE currentPosition = $currentPosition")
                // 滑动时 通过这里实现左右滑无限循环
                // 自动滑动时需设置动画，否则不走这里
                if (currentPosition == 0) {
                    // 当前显示第一张图片的时候，左滑后显示最后一张
                    // currentPosition = 0 setCurrentItem  3
                    //   binding.viewPager.setCurrentItem(adapter.itemCount - 2, false)
                    // 设置动画，并把滚动时间时间设置为0
                    // 防止 setCurrentItem 跨多页闪现问题
                    MyPagerHelper.setCurrentItem(getViewPager2(), adapter.itemCount - 2, 0)
                    Log.d(TAG,"after setCurrentItem  ${adapter.itemCount - 2} duration 0 currentPosition = 0")
                } else if (currentPosition == adapter.itemCount - 1) {
                    //当前显示最后一张图片的时候，右滑后显示第一张
                    // currentPosition = 4 setCurrentItem  1
                    //   binding.viewPager.setCurrentItem(1, false)
                    MyPagerHelper.setCurrentItem(getViewPager2(), 1, 0)
                    Log.d(TAG,"after setCurrentItem 1 duration 0")
                }
            }
            ViewPager.SCROLL_STATE_DRAGGING -> {
                Log.d(TAG,"inner SCROLL_STATE_DRAGGING")
            }
            else -> {}
        }
    }


}