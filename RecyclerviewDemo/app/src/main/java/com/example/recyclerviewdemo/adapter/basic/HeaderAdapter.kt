package com.example.recyclerviewdemo.adapter.basic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewdemo.R

class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_header, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {}

    override fun getItemCount(): Int = 1
}