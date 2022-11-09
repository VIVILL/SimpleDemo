package com.example.recyclerviewdemo.adapter.advanced

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewdemo.databinding.ItemBodyBinding

class BodyAdapter(var dataList: List<String>) :
    RecyclerView.Adapter<BodyAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemBodyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(string: String) {
            binding.stringTextview.text = string
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBodyBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataList[position])

    override fun getItemCount(): Int = dataList.size
}