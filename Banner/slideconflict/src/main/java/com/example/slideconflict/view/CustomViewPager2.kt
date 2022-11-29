package com.example.slideconflict.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.slideconflict.adapter.HeaderItemAdapter
import com.example.slideconflict.databinding.LayoutMyViewpager2Binding
import com.example.slideconflict.util.MyPagerHelper

private const val TAG = "MyViewPager2"

class CustomViewPager2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle){
    private var binding: LayoutMyViewpager2Binding
    private lateinit var adapter: HeaderItemAdapter

    var adapterSize: Int = 0

    // 作用: 1.离屏后恢复位置 2.左右无限滑
    // 定义初次加载时的位置为 1 , 每次更新位置后保存下数据
    var bannerPosition: Int = 1

    private lateinit var onTouchEventListener: (ev: MotionEvent)  -> Unit
    fun setTouchEventListener(onTouchEventListener : (ev: MotionEvent)  -> Unit){
        this.onTouchEventListener = onTouchEventListener
    }

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
                Log.d(TAG,"in 1..adapterSize-2 setCurrentItem")
                getViewPager2().setCurrentItem(position,false)
                binding.linearLayout.setSelected(position-1)
            }
            0 -> {
                getViewPager2().setCurrentItem(adapterSize-2,false)
                binding.linearLayout.setSelected(adapterSize-3)
            }
            adapterSize-1 -> {
                getViewPager2().setCurrentItem(1,false)
                binding.linearLayout.setSelected(0)
            }
        }
    }

    init {
        Log.d(TAG,"inner init")
        binding = LayoutMyViewpager2Binding.inflate(LayoutInflater.from(context),this,true)

        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                Log.d(TAG,"inner onViewDetachedFromWindow")
                unRegister()
            }

            override fun onViewAttachedToWindow(v: View?) {
                Log.d(TAG,"inner onViewAttachedToWindow")
                register()
            }
        })
    }


    fun getViewPager2(): ViewPager2 {
        return binding.viewPager2
    }


    /**
     * 手指触摸时停止自动轮播,抬起手指后，5s后开启
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG,"inner dispatchTouchEvent ev = $ev")
        // 将 MotionEvent 回传
        onTouchEventListener(ev)
        return super.dispatchTouchEvent(ev)
    }

    // 创建圆点指示器
    fun createCircle(size: Int){
        binding.linearLayout.createCircle(/*adapter.itemCount - 3*/size)
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
            bannerPosition = position
            Log.d(TAG, "inner onPageSelected: position = $position")
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
                Log.d(TAG,"inner SCROLL_STATE_IDLE bannerPosition = $bannerPosition")
                // 滑动时 通过这里实现左右滑无限循环
                // 自动滑动时需设置动画，否则不走这里
                if (bannerPosition == 0) {
                    // 当前显示第一张图片的时候，左滑后显示最后一张
                    // currentPosition = 0 setCurrentItem  3
                    //   binding.viewPager.setCurrentItem(adapter.itemCount - 2, false)
                    // 设置动画，并把滚动时间时间设置为0
                    // 防止 setCurrentItem 跨多页闪现问题
                    MyPagerHelper.setCurrentItem(getViewPager2(), adapter.itemCount - 2, 0)
                    Log.d(TAG,"after setCurrentItem  ${adapter.itemCount - 2} duration 0 currentPosition = 0")
                } else if (bannerPosition == adapter.itemCount - 1) {
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