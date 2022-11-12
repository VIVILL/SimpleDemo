package com.example.flow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.flow.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener{
/*            viewModel.login( binding.etUserName.text.toString(),
                binding.etPassword.text.toString())*/
            viewModel.loginBySharedFlow( binding.etUserName.text.toString(),
                binding.etPassword.text.toString())
        }

        lifecycleScope.launch {
            // https://developer.android.com/kotlin/flow/stateflow-and-sharedflow?hl=zh-cn
            // 如果需要更新界面，切勿使用 launch 或 launchIn 扩展函数从界面直接收集数据流。
            // 即使 View 不可见，这些函数也会处理事件。此行为可能会导致应用崩溃。
            // 为避免这种情况，请使用 repeatOnLifecycle API
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginUiState.collect {
                    when (it) {
                        is LoginUiState.Success -> {
                            Snackbar.make(
                                binding.root,
                                "Successfully logged in",
                                Snackbar.LENGTH_LONG
                            ).show()
                            binding.progressBar.isVisible = false
                        }
                        is LoginUiState.Error -> {
                            Snackbar.make(
                                binding.root,
                                it.message,
                                Snackbar.LENGTH_LONG
                            ).show()
                            binding.progressBar.isVisible = false
                        }
                        is LoginUiState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        else -> Unit
                    }
                }

            }
        }

        // Jetpack MVVM 常见错误二：在 launchWhenX 中启动协程  
        // https://www.jianshu.com/p/567b19cd5ebf
        lifecycleScope.launch {
            // https://developer.android.com/kotlin/flow/stateflow-and-sharedflow?hl=zh-cn
            // 如果需要更新界面，切勿使用 launch 或 launchIn 扩展函数从界面直接收集数据流。
            // 即使 View 不可见，这些函数也会处理事件。此行为可能会导致应用崩溃。
            // 为避免这种情况，请使用 repeatOnLifecycle API
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginUiAction.collect {
                    when (it) {
                        is LoginUiAction.Success -> {
                            Snackbar.make(
                                binding.root,
                                "Successfully logged in",
                                Snackbar.LENGTH_LONG
                            ).show()
                            binding.progressBar.isVisible = false
                        }
                        is LoginUiAction.Error -> {
                            Snackbar.make(
                                binding.root,
                                it.message,
                                Snackbar.LENGTH_LONG
                            ).show()
                            binding.progressBar.isVisible = false
                        }
                        is LoginUiAction.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        else -> Unit
                    }
                }
            }
        }

    }


}