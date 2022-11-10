package com.example.flow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "FlowTestViewModel"
class FlowTestViewModel: ViewModel() {
    // 使用 Repository
    private val repository: Repository = Repository()

    // 使用 flow
    private var _numberFlow = repository.getRandomNumber()
    val numberFlow: Flow<Int> = _numberFlow

    fun updateNumberFlow() {
        // 更新 _countStateFlow
        Log.d(TAG,"inner updateNumberFlow")
        _numberFlow = repository.getRandomNumber()
    }

    // 使用 StateFlow
    // StateFlow 具有一个属性 value，可以被设置
    // 设置默认值 0
    private val _numberStateFlow = MutableStateFlow<Int>(0)
    val numberStateFlow: StateFlow<Int> = _numberStateFlow

    fun updateNumberStateFlow() {
        // 更新 _countStateFlow 的 value 数据
        viewModelScope.launch {
            _numberStateFlow.value = repository.getRandomNumber()
                .stateIn(viewModelScope)
                .value
        }
    }

}