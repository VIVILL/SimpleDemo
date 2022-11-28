package com.example.banner.viewmodel

import android.util.Log
import android.view.MotionEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.example.banner.MyPagerHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

private const val TAG = "BannerViewModel"

class BannerViewModel: ViewModel() {

    private val _autoScrollAction = MutableSharedFlow<AutoScrollAction>()
    val autoScrollAction: SharedFlow<AutoScrollAction> = _autoScrollAction

    private var isAutoScroll: Boolean = true

    lateinit var autoScrollJob: Job


    fun autoScroll() {
        Log.d(TAG,"inner autoScroll isAutoScroll = $isAutoScroll")
        if (this::autoScrollJob.isInitialized) {
            autoScrollJob.cancel()
            Log.d(TAG, "after autoScrollJob.cancel()")
        }
        isAutoScroll = true
        autoScrollJob = viewModelScope.launch {
            repeat(Int.MAX_VALUE){
                Log.d(TAG,"inner repeat")
                delay(3000L)
                Log.d(TAG,"after delay 3000L isAutoScroll = $isAutoScroll")
                if (isAutoScroll){
                    _autoScrollAction.emit(AutoScrollAction.AutoScroll)
                }
            }
        }
    }

    fun cancelAutoScroll() {
        Log.d(TAG,"inner cancelAutoScroll")
        isAutoScroll = false
        if (this::autoScrollJob.isInitialized) {
            autoScrollJob.cancel()
            Log.d(TAG, "after autoScrollJob.cancel()")
        }
    }

    private lateinit var pauseAutoScrollJob: Job

    fun touch(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "inner MotionEvent.ACTION_DOWN")
                isAutoScroll = false
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL-> {
                Log.d(TAG, "inner MotionEvent.ACTION_UP or ACTION_CANCEL")
                // 先取消之前的任务
                if (::pauseAutoScrollJob.isInitialized) {
                    pauseAutoScrollJob.cancel()
                    Log.d(TAG, "after job.cancel()")
                }
                Log.d(TAG, "after set autoScroll = false")
                pauseAutoScrollJob = viewModelScope.launch{
                    Log.d(TAG, "after launch")
                    // 等待5s后 重新开始 无限循环
                    delay(5000L)
                    isAutoScroll = true
                    Log.d(TAG, "after delay 5000L, after set autoScroll = true")
                }
            }
            else -> {}
        }
    }


}

sealed class AutoScrollAction {
    object AutoScroll: AutoScrollAction()
}

sealed class TouchAction {
    data class Touch(val event: MotionEvent): TouchAction()
}