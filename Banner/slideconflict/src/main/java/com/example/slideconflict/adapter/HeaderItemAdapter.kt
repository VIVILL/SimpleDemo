package com.example.slideconflict.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.slideconflict.databinding.ItemHeaderBinding

private const val TAG = "HeaderItemAdapter"
class HeaderItemAdapter (var dataList: List<String>, private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<HeaderItemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String, onClick: (String) -> Unit) {
            loadImage(itemView.context,binding.imageView,url)
            binding.imageView.setOnClickListener {
                onClick(url)
            }
        }

        private fun loadImage(context: Context, imageView: ImageView, imageUrl: String) {
            val requestOption = RequestOptions()
            Glide.with(context).load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .listener(ImageRequestListener(object :ImageRequestListener.Callback{
                    override fun onFailure(message: String?) {
                        Log.e(TAG,"Fail to load: $message")
                    }

                    override fun onSuccess(dataSource: String) {
                        Log.d(TAG,"Loaded from: $dataSource")
                    }
                }))
                .into(imageView)
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

    override fun getItemCount(): Int {
        return dataList.size
    }
}