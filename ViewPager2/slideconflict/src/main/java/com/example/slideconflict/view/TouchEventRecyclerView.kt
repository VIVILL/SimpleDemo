package com.example.slideconflict.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "TouchEventRecyclerView"

class TouchEventRecyclerView: RecyclerView {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    private lateinit var onTouchEventListener: (ev: MotionEvent)  -> Unit
    fun setTouchEventListener(onTouchEventListener : (ev: MotionEvent)  -> Unit){
        this.onTouchEventListener = onTouchEventListener
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG,"inner dispatchTouchEvent")
        // 将 MotionEvent 回传
        onTouchEventListener(ev)
        return super.dispatchTouchEvent(ev)
    }

}