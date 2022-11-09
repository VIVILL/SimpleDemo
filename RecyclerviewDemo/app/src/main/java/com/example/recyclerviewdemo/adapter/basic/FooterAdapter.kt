package com.example.recyclerviewdemo.adapter.basic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewdemo.R

class FooterAdapter : RecyclerView.Adapter<FooterAdapter.FooterViewHolder>() {

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterViewHolder {
        return FooterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_footer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FooterViewHolder, position: Int) {}

    override fun getItemCount(): Int = 1
}