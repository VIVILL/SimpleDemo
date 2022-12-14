package com.example.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.example.flow.databinding.ActivityMainBinding
import com.example.flow.viewmodel.FlowTestViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
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


        /* https://juejin.cn/post/7127454075666300965
        * 冷流 :只有订阅者订阅时，才开始执行发射数据流的代码。
        * 并且冷流和订阅者只能是一对一的关系，当有多个不同的订阅者时，消息是重新完整发送的。
        * 也就是说对冷流而言，有多个订阅者的时候，他们各自的事件是独立的。
        * 热流:无论有没有订阅者订阅，事件始终都会发生。
        * 当 热流有多个订阅者时，热流与订阅者们的关系是一对多的关系，可以与多个订阅者共享信息。
        * */
        binding.flowButton.setOnClickListener{
            // flow 是冷流 只有当订阅者发起订阅时，事件的发送者才会开始发送事件。
            // 每次调用创建flow 都需调用 collect
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.numberFlow.collect { value ->
                        Log.d(TAG,"numberFlow = $value")
                    }
                }
            }
        }

        // StateFlow 和 SharedFlow 是热流
        binding.stateflowButton.setOnClickListener{
            // 更新 StateFlow 中的 value
             viewModel.updateNumberStateFlow()
        }
        binding.sharedFlowButton.setOnClickListener{
            viewModel.updateNumberSharedFlow()
        }

        subscribe()
    }

    private fun subscribe(){
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.numberSharedFlow.collect { value ->
                    Log.d(TAG,"numberSharedFlow = $value")
                }
            }
        }

    }


}