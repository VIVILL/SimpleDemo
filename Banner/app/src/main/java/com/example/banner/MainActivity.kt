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

    private var position: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val urlData = listOf(
            //最后一张图片
            "http://t8.baidu.com/it/u=198337120,441348595&fm=79&app=86&f=JPEG?w=1280&h=732",

            "http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&f=JPEG?w=1280&h=853",
            "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/a6efce1b9d16fdfabf36882ab08f8c5495ee7b9f.jpg",
            "http://t8.baidu.com/it/u=198337120,441348595&fm=79&app=86&f=JPEG?w=1280&h=732",

            //第一张图片
            "http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&f=JPEG?w=1280&h=853"
        )

        val headerItemAdapter = HeaderItemAdapter(urlData){
            Log.d(TAG,"onclick it = $it")
        }
        val headerAdapter = HeaderAdapter(headerItemAdapter)
        headerAdapter.setPositionListener { position ->
            Log.d(TAG,"setPositionListener position = $position")
            this.position = position
        }
        headerAdapter.setOnBindListener{
            Log.d(TAG,"setOnBindListener bannerPosition = $position")
            return@setOnBindListener position
        }

        val bodyStringList = listOf("aaa","bbb","ccc","aaa","bbb","ccc"
            ,"aaa","bbb","ccc","aaa","bbb","ccc","aaa","bbb","ccc"
            ,"aaa","bbb","ccc","aaa","bbb","ccc","aaa","bbb","ccc")

        val bodyAdapter = BodyAdapter(bodyStringList)

        val concatAdapter =  ConcatAdapter(headerAdapter,bodyAdapter)

        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.recyclerView.adapter = concatAdapter

        // 设置需要缓存的 ViewHolder数量 防止离屏后再显示时 频繁执行 onBindViewHolder
        binding.recyclerView.setItemViewCacheSize(10)
    }

}