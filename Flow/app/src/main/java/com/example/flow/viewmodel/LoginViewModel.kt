package com.example.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * StateFlow 和 SharedFlow 简单使用
 * */
class LoginViewModel: ViewModel() {
    // https://blog.csdn.net/fly_with_24/article/details/120300290
    // 使用 StateFlow （即UiState）的时候 ，有与 LiveData 一样的「粘性事件」问题
    // 在弹出 Snackbar 之后旋转屏幕后， 会再次弹出 Snackbar
    // 使用 SharedFlow 不会有「粘性事件」的问题
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            delay(2000L)
            if(username == "abc" && password == "123") {
                _loginUiState.value = LoginUiState.Success
            } else {
                _loginUiState.value = LoginUiState.Error("Wrong credentials")
            }
        }
    }

    // 使用 SharedFlow
    private val _loginUiAction = MutableSharedFlow<LoginUiAction>()
    val loginUiAction: SharedFlow<LoginUiAction> = _loginUiAction

    fun loginBySharedFlow(username: String, password: String) {
        viewModelScope.launch {
            _loginUiAction.emit(LoginUiAction.Loading)
            delay(2000L)
            if(username == "abc" && password == "123") {
                _loginUiAction.emit(LoginUiAction.Success)
            } else {
                _loginUiAction.emit(LoginUiAction.Error("Wrong credentials"))
            }
        }
    }
}


sealed class LoginUiState {
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    object Loading : LoginUiState()
    object Empty : LoginUiState()
}

sealed class LoginUiAction {
    object Success : LoginUiAction()
    data class Error(val message: String) : LoginUiAction()
    object Loading : LoginUiAction()
}
