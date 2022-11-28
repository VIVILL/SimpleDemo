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

    var isAutoScroll: Boolean = true

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

    private val _touchAction = MutableSharedFlow<TouchAction>()
    val touchAction: SharedFlow<TouchAction> = _touchAction

    fun touch(event: MotionEvent) {
        viewModelScope.launch {
            _touchAction.emit(TouchAction.Touch(event))
        }
    }

}

sealed class AutoScrollAction {
    object AutoScroll: AutoScrollAction()
}

sealed class TouchAction {
    data class Touch(val event: MotionEvent): TouchAction()
}