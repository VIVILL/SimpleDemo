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

        private lateinit var onPositionListener: (position: Int)  -> Unit
        fun setPositionListener(onPositionListener : (position: Int)  -> Unit){
            this.onPositionListener = onPositionListener
        }

        init {
            itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                // View onDetached 的时候回调 onDestroy()
                override fun onViewDetachedFromWindow(v: View?) {
                    //   itemView.removeOnAttachStateChangeListener(this)
                    val currentPosition  = binding.viewPager.getViewPager2CurrentItem()
                    Log.d(TAG,"currentPosition = $currentPosition")
                    // 将 position 数据回传
                    onPositionListener(currentPosition)
                    onDestroy()
                }

                // View onAttached 的时候回调 onCreate()
                override fun onViewAttachedToWindow(v: View?) {
                    onCreate()
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

        fun bind(adapter: HeaderItemAdapter,onBindListener : ()  -> Int) {
            Log.d(TAG,"inner bind adapter count = ${adapter.itemCount}")
            // 保存离屏前位置，并在重新显示时恢复位置
            val position = onBindListener()
            Log.d(TAG,"onBindListener position = $position")

            binding.viewPager.setAdapter(adapter)
            binding.viewPager.createCircle(adapter.itemCount - 3)
            binding.viewPager.register()
            // 初次加载 设置显示 位置为1 的数据
           // binding.viewPager.setPosition(1)
            binding.viewPager.setPosition(position)
            // 开启自动滚动
            binding.viewPager.autoScroll()

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

    private lateinit var onBindListener: ()  -> Int
    fun setOnBindListener(onBindListener : ()  -> Int){
        this.onBindListener = onBindListener
    }

    private lateinit var onPositionListener: (position: Int)  -> Unit
    fun setPositionListener(onPositionListener : (position: Int)  -> Unit){
        this.onPositionListener = onPositionListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.register()
        holder.setPositionListener(onPositionListener)
        holder.bind(adapter,onBindListener)
    }

    override fun getItemCount(): Int = 1
}