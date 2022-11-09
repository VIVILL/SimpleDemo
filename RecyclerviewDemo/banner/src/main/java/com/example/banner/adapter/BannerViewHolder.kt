package com.example.banner.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banner.bean.Banner
import com.example.banner.databinding.ItemImageBinding

class BannerViewHolder(private val binding: ItemImageBinding)
    : RecyclerView.ViewHolder(binding.root){

    fun bind(banner: Banner) {
        Glide.with(itemView)
            .load(banner.imagePath)
            .into(binding.itemImage)
    }
}