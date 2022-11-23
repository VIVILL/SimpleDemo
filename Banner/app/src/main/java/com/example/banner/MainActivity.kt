package com.example.banner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.banner.adapter.BodyAdapter
import com.example.banner.adapter.HeaderAdapter
import com.example.banner.adapter.HeaderItemAdapter
import com.example.banner.databinding.ActivityMainBinding


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val urlData = listOf(
            "http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&f=JPEG?w=1280&h=853",
            "https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/0824ab18972bd40797d8db1179899e510fb3093a.jpg",
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Flmg.jj20.com%2Fup%2Fallimg%2F4k%2Fs%2F02%2F2109242302423522-0-lp.jpg&refer=http%3A%2F%2Flmg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1671755538&t=bc46a5f3f25c1ecafa839a1f5a649b1e",
            "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/a6efce1b9d16fdfabf36882ab08f8c5495ee7b9f.jpg",
            "http://t8.baidu.com/it/u=198337120,441348595&fm=79&app=86&f=JPEG?w=1280&h=732"
        )

        val headerItemAdapter = HeaderItemAdapter(urlData){
            Log.d(TAG,"onclick it = $it")
        }
        val headerAdapter = HeaderAdapter(headerItemAdapter)

        val bodyStringList = listOf("aaa","bbb","ccc","aaa","bbb","ccc"
            ,"aaa","bbb","ccc","aaa","bbb","ccc","aaa","bbb","ccc"
            ,"aaa","bbb","ccc","aaa","bbb","ccc","aaa","bbb","ccc")

        val bodyAdapter = BodyAdapter(bodyStringList)

        val concatAdapter =  ConcatAdapter(headerAdapter,bodyAdapter)

        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.recyclerView.adapter = concatAdapter
    }

}