package com.example.slideconflict.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slideconflict.databinding.ItemStringBinding

class StringAdapter(var dataList: List<String>) :
    RecyclerView.Adapter<StringAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemStringBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(string: String) {
            binding.stringTextview.text = string
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemStringBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataList[position])

    override fun getItemCount(): Int = dataList.size
}