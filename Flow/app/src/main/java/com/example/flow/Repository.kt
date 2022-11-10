package com.example.flow

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext


class Repository {

    fun getRandomNumber(): Flow<Int>{
        // 创建 Flow
        return flow {
            // 模拟耗时任务
            // 会在后台线程执行delay ，执行完后，自动切换线程
            delay(1_000)
            // 随机输出 number
            val number = (0..100).random()
            Log.d("Repository","number = $number")
            // 发射 number
            emit(number)
            Log.d("Repository","emit number = $number")
        }
    }

}
