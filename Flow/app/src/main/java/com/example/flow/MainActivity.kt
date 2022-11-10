package com.example.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.flow.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(FlowTestViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 因为 使用了lifecycleScope,我们的collector具有了生命周期的感应能力
        // 每次 _numberStateFlow 的 value 更新的时候，collector就会执行相应的代码，
        lifecycleScope.launch {
            // https://developer.android.com/kotlin/flow/stateflow-and-sharedflow?hl=zh-cn
            // 如果需要更新界面，切勿使用 launch 或 launchIn 扩展函数从界面直接收集数据流。
            // 即使 View 不可见，这些函数也会处理事件。此行为可能会导致应用崩溃。
            // 为避免这种情况，请使用 repeatOnLifecycle API
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.numberStateFlow.collect { value ->
                    Log.d(TAG,"numberStateFlow = $value")
                }
            }
        }

        binding.flowButton.setOnClickListener{
            // flow 的监听要写在这里，否则后续点击button后不更新
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.numberFlow.collect { value ->
                        Log.d(TAG,"numberFlow = $value")
                    }
                }
            }
            // 更新 Flow
            viewModel.updateNumberFlow()
        }

        binding.stateflowButton.setOnClickListener{
            // 更新 StateFlow 中的 value
             viewModel.updateNumberStateFlow()
        }
    }
}