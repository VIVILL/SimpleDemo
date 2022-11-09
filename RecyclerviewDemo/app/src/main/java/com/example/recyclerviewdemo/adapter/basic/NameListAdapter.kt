package com.example.recyclerviewdemo.adapter.basic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewdemo.R

class NameListAdapter : ListAdapter<String, NameListAdapter.NameViewHolder>(NameDiffCallback) {

    class NameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTv: TextView = view.findViewById(R.id.name_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        return NameViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_name, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.nameTv.text = getItem(position)
    }

}

object NameDiffCallback : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}