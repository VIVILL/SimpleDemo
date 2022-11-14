package com.example.jetpackdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jetpackdemo.bean.Banner
import com.example.jetpackdemo.databinding.ItemImageBinding

class BannerAdapter(var bannerList: List<Banner>): RecyclerView.Adapter<BannerAdapter.BannerViewHolder> () {
    class BannerViewHolder(private val binding: ItemImageBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(banner: Banner) {
            Glide.with(itemView)
                .load(banner.imagePath)
                .into(binding.itemImage)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        // 通过 getItem 获取数据
        if (bannerList.isEmpty()){
            return
        }
        val banner = bannerList.get(position % bannerList.size)
        banner.let { holder.bind(it) }

    }


    override fun getItemCount(): Int {
        // return Int.MAX_VALUE
        if (bannerList.isEmpty()){
            return 0
        }
        return  Integer.MAX_VALUE
    }
}
