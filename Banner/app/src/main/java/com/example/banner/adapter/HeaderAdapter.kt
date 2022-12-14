package com.example.banner.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.banner.MyPagerHelper
import com.example.banner.databinding.ItemParentHeaderBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "HeaderAdapter"
class HeaderAdapter(private val adapter: HeaderItemAdapter) :
    RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemParentHeaderBinding) : RecyclerView.ViewHolder(binding.root){
        private lateinit var onTouchEventListener: (ev: MotionEvent)  -> Unit

        fun setTouchEventListener(onTouchEventListener : (ev: MotionEvent)  -> Unit){
            this.onTouchEventListener = onTouchEventListener
        }

        fun bind(adapter: HeaderItemAdapter) {
            Log.d(TAG,"inner bind")
            binding.viewPager.setAdapter(adapter)
            binding.viewPager.createCircle(adapter.itemCount - 3)
            binding.viewPager.setTouchEventListener(onTouchEventListener)
        }

    }

    lateinit var binding: ItemParentHeaderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemParentHeaderBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    private lateinit var onViewAttachedListener: () -> Unit
    private lateinit var onViewDetachedListener: ()  -> Unit
    private lateinit var onTouchEventListener: (ev: MotionEvent)  -> Unit

    fun setOnViewAttachedListener(onViewAttachedListener: () -> Unit) {
        this.onViewAttachedListener = onViewAttachedListener
    }

    fun setOnViewDetachedListener(onViewDetachedListener : ()  -> Unit){
        this.onViewDetachedListener = onViewDetachedListener
    }

    fun setTouchEventListener(onTouchEventListener : (ev: MotionEvent)  -> Unit){
        this.onTouchEventListener = onTouchEventListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setTouchEventListener(onTouchEventListener)
        holder.bind(adapter)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        Log.d(TAG,"inner onViewAttachedToWindow")
        super.onViewAttachedToWindow(holder)
        binding.viewPager.setPosition(binding.viewPager.bannerPosition)
        onViewAttachedListener()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        Log.d(TAG,"inner onViewDetachedFromWindow")
        super.onViewDetachedFromWindow(holder)
        onViewDetachedListener()
    }

    override fun getItemCount(): Int = 1

    // ??????????????????????????? ??????????????? ????????????
    fun setCurrentItem(){
        if (::binding.isInitialized){
            MyPagerHelper.setCurrentItem(
                binding.viewPager.getViewPager2(),
                binding.viewPager.getViewPager2().currentItem + 1,
                300
            )
        }
    }
}