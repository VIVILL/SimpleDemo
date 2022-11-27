package com.example.jetpackdemo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackdemo.databinding.ItemHeaderBinding

class HeaderAdapter(private var adapter: BannerAdapter) :
    RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(adapter: BannerAdapter) {
            val context = binding.root.context
            binding.headerList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.headerList.adapter = adapter
        }
    }

    private lateinit var binding: ItemHeaderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemHeaderBinding.inflate(layoutInflater, parent, false)
        isAbleScroll = true
        // 确保一次只能滑动一个数据，停止的时候图片的位置正确
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.headerList)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("HeaderAdapter","inner onBindViewHolder")
        holder.bind(adapter)
    }

    override fun getItemCount(): Int = 1

    var isAbleScroll: Boolean = false
    fun smoothScrollToPosition(position: Int){
        if (isAbleScroll){
            binding.headerList.smoothScrollToPosition(position)
        }
    }
}