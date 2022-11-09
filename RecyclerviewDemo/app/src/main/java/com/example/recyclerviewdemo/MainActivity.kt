package com.example.recyclerviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewdemo.adapter.basic.FooterAdapter
import com.example.recyclerviewdemo.adapter.basic.HeaderAdapter
import com.example.recyclerviewdemo.adapter.basic.NameListAdapter

class MainActivity : AppCompatActivity() {
    private val mHeaderAdapter by lazy {
        HeaderAdapter()
    }
    private val mFooterAdapter by lazy {
        FooterAdapter()
    }


    private val mNameListAdapter by lazy {
        NameListAdapter()
    }

    private val mConcatAdapter by lazy {
        ConcatAdapter(mHeaderAdapter, mNameListAdapter, mFooterAdapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<RecyclerView>(R.id.recyclerView_list).apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = mConcatAdapter
        }
        mNameListAdapter.submitList(
            listOf(
                "Anna",
                "Bruce",
                "Cindy",
                "Diana",
                "Edison",
                "Ford"
            )
        )

    }
}