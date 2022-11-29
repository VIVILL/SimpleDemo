package com.example.slideconflict.viewmodel

import android.view.MotionEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class TouchViewModel: ViewModel() {

    private val _recyclerviewTouchAction = MutableSharedFlow<TouchAction>()
    val recyclerviewTouchAction: SharedFlow<TouchAction> = _recyclerviewTouchAction

    fun touchRecyclerview(event: MotionEvent) {
        viewModelScope.launch {
            _recyclerviewTouchAction.emit(TouchAction.Touch(event))
        }
    }

}

sealed class TouchAction {
    data class Touch(val event: MotionEvent): TouchAction()
}