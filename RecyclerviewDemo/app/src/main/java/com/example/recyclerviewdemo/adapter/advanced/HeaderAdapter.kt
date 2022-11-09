package com.example.recyclerviewdemo.adapter.advanced

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewdemo.databinding.ItemParentHeaderBinding

class HeaderAdapter(private val adapter: HeaderItemAdapter) :
    RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemParentHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(adapter: HeaderItemAdapter) {
            val context = binding.root.context
            binding.headerList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.headerList.addItemDecoration(
                DividerItemDecoration(
                    binding.headerList.context,
                    (binding.headerList.layoutManager as LinearLayoutManager).orientation
                )
            )
            binding.headerList.adapter = adapter
            // attachToRecyclerView 放这里会报错
            // 确保一次只能滑动一个数据，停止的时候图片的位置正确
/*            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.headerList)*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemParentHeaderBinding.inflate(layoutInflater, parent, false)
        // 确保一次只能滑动一个数据，停止的时候图片的位置正确
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.headerList)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(adapter)
    }

    override fun getItemCount(): Int = 1
}