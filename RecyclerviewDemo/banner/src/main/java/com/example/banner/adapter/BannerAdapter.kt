package com.example.banner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.banner.bean.Banner
import com.example.banner.databinding.ItemImageBinding

class BannerAdapter(val bannerList: MutableList<Banner>): RecyclerView.Adapter< BannerViewHolder> () {

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
        if (bannerList.size == 0){
            return
        }
        val banner = bannerList.get(position % bannerList.size)
        banner.let { holder.bind(it) }

    }


    override fun getItemCount(): Int {
        // return Int.MAX_VALUE
        if (bannerList.size == 0){
            return 0
        }
        return  Integer.MAX_VALUE
    }
}
