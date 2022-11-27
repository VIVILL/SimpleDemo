package com.example.banner.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.banner.databinding.ItemParentHeaderBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "HeaderAdapter"
class HeaderAdapter(private val adapter: HeaderItemAdapter) :
    RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemParentHeaderBinding) : RecyclerView.ViewHolder(binding.root), LifecycleOwner{
        private lateinit var lifecycleRegistry: LifecycleRegistry

        init {
            itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                // View onDetached 的时候回调 onDestroy()
                override fun onViewDetachedFromWindow(v: View?) {
                    //   itemView.removeOnAttachStateChangeListener(this)
                    Log.d(TAG,"inner onViewAttachedToWindow position = ${binding.viewPager.bannerPosition}")
                    onDestroy()
                }

                // View onAttached 的时候回调 onCreate()
                override fun onViewAttachedToWindow(v: View?) {
                    Log.d(TAG,"inner onViewAttachedToWindow position = ${binding.viewPager.bannerPosition}")
                    onCreate()

                    binding.viewPager.register()
                    binding.viewPager.setPosition(binding.viewPager.bannerPosition)
                    // 开启自动滚动
                    binding.viewPager.autoScroll()
                }
            })
        }

        // 注册
        fun register(){
            lifecycleRegistry = LifecycleRegistry(this)
            Log.d(TAG,"after lifecycleRegistry")
        }

        fun onCreate() {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }

        fun onDestroy() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        fun bind(adapter: HeaderItemAdapter) {
            Log.d(TAG,"inner bind")

            binding.viewPager.setAdapter(adapter)
            binding.viewPager.createCircle(adapter.itemCount - 3)
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemParentHeaderBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.register()
        holder.bind(adapter)
    }

    override fun getItemCount(): Int = 1
}