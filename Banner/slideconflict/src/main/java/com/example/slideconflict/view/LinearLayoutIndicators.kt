package com.example.slideconflict.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.view.LayoutInflater
import android.view.View
import com.example.slideconflict.R
import com.example.slideconflict.databinding.LayoutIndicatorsBinding

private const val TAG = "LinearLayoutIndicators"
class LinearLayoutIndicators : LinearLayout {
    private lateinit var binding: LayoutIndicatorsBinding

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        binding = LayoutIndicatorsBinding.inflate(LayoutInflater.from(context),this,true)
        Log.d(TAG,"inner constructor")
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    private var size: Int = 0
    fun createCircle(size: Int) {
        // 先清除一下view，否则在多次执行 后会重复添加多个
        binding.linearLayout.removeAllViews()

        this.size = size
        Log.d(TAG ,"inner createCircle size = $size ")
        for (i in 0..size) {
            val view = View(context)
            view.setBackgroundResource(R.drawable.circle_indicator)
            view.isSelected = false
            // 设置 大小
            val params = LayoutParams(20, 20)
            //设置间隔
            if (i != 0) {
                params.leftMargin = 20
            }
            Log.d(TAG ,"addView")
            binding.linearLayout.addView(view,i, params)
        }
    }


    fun setSelected(position: Int){
        binding.linearLayout.getChildAt(position).isSelected = true
        for (i in 0..size) {
            if (i != position){
                Log.d(TAG ,"i = $i")
                binding.linearLayout.getChildAt(i).isSelected = false
            }
        }
    }

}