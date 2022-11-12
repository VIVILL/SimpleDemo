package com.example.flow.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "FlowTestViewModel"
class FlowTestViewModel: ViewModel() {
    // 使用 Repository
    private val repository: Repository = Repository()

    // 使用 flow
    private val _numberFlow = repository.getRandomNumber()
    val numberFlow: Flow<Int> = _numberFlow

    // 使用 StateFlow
    // StateFlow 具有一个属性 value，可以被设置
    // 设置默认值 0
    private val _numberStateFlow = MutableStateFlow<Int>(0)
    val numberStateFlow: StateFlow<Int> = _numberStateFlow

    fun updateNumberStateFlow() {
        // 更新  value 数据
        viewModelScope.launch {
            _numberStateFlow.value = repository.getRandomNumber()
                .stateIn(viewModelScope)
                .value
        }
    }

    // 使用 SharedFlow
    private val _numberSharedFlow = MutableSharedFlow<Int>()
    val numberSharedFlow: SharedFlow<Int> = _numberSharedFlow

    fun updateNumberSharedFlow() {
        viewModelScope.launch {
            repository.getRandomNumber().collect {
                _numberSharedFlow.emit(it)
            }
        }
    }

}