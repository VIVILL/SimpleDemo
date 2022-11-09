package com.example.recyclerviewdemo.adapter.advanced

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewdemo.databinding.ItemHeaderBinding

class HeaderItemAdapter (var dataList: List<String>, private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<HeaderItemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(string: String, onClick: (String) -> Unit) {
            binding.headerStringTextview.text = string
            binding.root.setOnClickListener { onClick(string) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemHeaderBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (dataList.isEmpty()){
            return
        }
        holder.bind(dataList[position % dataList.size], onClick)
    }

    override fun getItemCount(): Int {//= dataList.size{
        if (dataList.isEmpty()){
            return 0
        }
        return  Integer.MAX_VALUE
    }
}