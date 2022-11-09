package com.example.recyclerviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.recyclerviewdemo.adapter.advanced.BodyAdapter
import com.example.recyclerviewdemo.adapter.advanced.HeaderAdapter
import com.example.recyclerviewdemo.adapter.advanced.HeaderItemAdapter
import com.example.recyclerviewdemo.adapter.basic.FooterAdapter
import com.example.recyclerviewdemo.adapter.basic.NameListAdapter
import com.example.recyclerviewdemo.databinding.ActivityAdvancedBinding

class AdvancedActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAdvancedBinding.inflate(layoutInflater)
    }

    private  var headStringList:List<String> = ArrayList<String>()
    private var bodyStringList:List<String> = ArrayList<String>()

    private lateinit var headerItemAdapter: HeaderItemAdapter

    private lateinit var headerAdapter: HeaderAdapter

    private lateinit var bodyAdapter: BodyAdapter

    private lateinit var concatAdapter: ConcatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_advanced)
        setContentView(binding.root)
        headerItemAdapter = HeaderItemAdapter(
            headStringList, onClick = { string ->
                    Log.d("AdvancedActivity","string = $string")
    })
        headerAdapter = HeaderAdapter(headerItemAdapter)
        bodyAdapter = BodyAdapter(bodyStringList)
        concatAdapter =  ConcatAdapter(headerAdapter,bodyAdapter)

        binding.advancedRecyclerViewList.adapter = concatAdapter
        binding.button.setOnClickListener {
            headStringList = listOf("a","b","c","d","e","f")
            bodyStringList = listOf("aaa","bbb","ccc","aaa","bbb","ccc"
            ,"aaa","bbb","ccc","aaa","bbb","ccc","aaa","bbb","ccc")

            headerItemAdapter.dataList = headStringList
            bodyAdapter.dataList = bodyStringList
            headerItemAdapter.notifyDataSetChanged()
            bodyAdapter.notifyDataSetChanged()
        }
    }
}