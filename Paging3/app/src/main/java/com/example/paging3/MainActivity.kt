package com.example.paging3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paging3.databinding.ActivityMainBinding
import com.example.paging3.recyclerview.ArticleAdapter
import com.example.paging3.viewmodel.ArticleViewModel
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val binDing by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val articleViewModel by lazy {
        ViewModelProvider(this,Injection.provideViewModelFactory(owner = this)).get(ArticleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binDing.root)

        val items = articleViewModel.items
        val articleAdapter = ArticleAdapter()

        // 给 recyclerview 绑定 adapter
        binDing.bindAdapter(articleAdapter = articleAdapter)

        // Collect from the Article Flow in the ViewModel, and submit it to the
        // ListAdapter.
        lifecycleScope.launch {
            // We repeat on the STARTED lifecycle because an Activity may be PAUSED
            // but still visible on the screen, for example in a multi window app
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 调用 下游终止操作符 collect
                items.collect {
                    Log.d(TAG,"collect size = ${it.size}")
                    if (it.isNotEmpty()){
                        Log.d(TAG,"first = ${it[0]}")
                    }
                    articleAdapter.submitList(it)
                }
            }
        }
    }

    private fun ActivityMainBinding.bindAdapter(articleAdapter: ArticleAdapter) {
        list.adapter = articleAdapter
        list.layoutManager = LinearLayoutManager(list.context)
        val decoration = DividerItemDecoration(list.context, DividerItemDecoration.VERTICAL)
        list.addItemDecoration(decoration)
    }
}