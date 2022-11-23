package com.example.banner.indicator


import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.viewpager2.widget.ViewPager2

/**
 * CircleIndicator work with ViewPager2
 */
class CircleIndicator : BaseCircleIndicator {
    private var mViewpager: ViewPager2? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context!!, attrs, defStyleAttr, defStyleRes
    ) {
    }

    fun setViewPager(viewPager: ViewPager2?) {
        mViewpager = viewPager
        if (mViewpager != null && mViewpager!!.adapter != null) {
            mLastPosition = -1
            createIndicators()
         /*   mViewpager!!.removeOnPageChangeListener(mInternalPageChangeListener)
            mViewpager!!.addOnPageChangeListener(mInternalPageChangeListener)*/
            mViewpager!!.unregisterOnPageChangeCallback(mInternalPageChangeListener)
            mViewpager!!.registerOnPageChangeCallback(mInternalPageChangeListener)
            mInternalPageChangeListener.onPageSelected(mViewpager!!.currentItem)
        }
    }

    private fun createIndicators() {
        val adapter = mViewpager!!.adapter
        val count: Int = adapter?.itemCount ?: 0
        createIndicators(count, mViewpager!!.currentItem)
    }

    // ViewPager2 滑动监听
    private val mInternalPageChangeListener: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback(){
        override fun onPageScrolled(
            position: Int, positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            if (mViewpager!!.adapter == null
                || mViewpager!!.adapter!!.itemCount <= 0
            ) {
                return
            }
            animatePageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
    // ViewPager 滑动监听
/*
    private val mInternalPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int, positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            if (mViewpager!!.adapter == null
                || mViewpager!!.adapter!!.itemCount <= 0
            ) {
                return
            }
            animatePageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
*/
}