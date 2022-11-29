package com.example.slideconflict.fragment.home.viewpager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slideconflict.R
import com.example.slideconflict.adapter.HeaderAdapter
import com.example.slideconflict.adapter.HeaderItemAdapter
import com.example.slideconflict.adapter.StringAdapter
import com.example.slideconflict.databinding.FragmentFirstBinding
import com.example.slideconflict.viewmodel.AutoScrollAction
import com.example.slideconflict.viewmodel.BannerViewModel
import com.example.slideconflict.viewmodel.TouchAction
import com.example.slideconflict.viewmodel.TouchViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlin.math.abs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "FirstFragment"
class FirstFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val touchViewModel: TouchViewModel by activityViewModels()

    private val bannerViewModel: BannerViewModel by viewModels()

    private lateinit var headerAdapter: HeaderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"inner onCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"inner onCreateView")
        // Inflate the layout for this fragment
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        binding.recyclerview.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerview.context,
                (binding.recyclerview.layoutManager as LinearLayoutManager).orientation
            )
        )

        val urlData = listOf(
            //最后一张图片
            "http://t8.baidu.com/it/u=198337120,441348595&fm=79&app=86&f=JPEG?w=1280&h=732",

            "http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&f=JPEG?w=1280&h=853",
            "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/a6efce1b9d16fdfabf36882ab08f8c5495ee7b9f.jpg",
            "http://t8.baidu.com/it/u=198337120,441348595&fm=79&app=86&f=JPEG?w=1280&h=732",

            //第一张图片
            "http://t8.baidu.com/it/u=1484500186,1503043093&fm=79&app=86&f=JPEG?w=1280&h=853"
        )

        val headerItemAdapter = HeaderItemAdapter(urlData){
            Log.d(TAG,"onclick it = $it")
        }
        headerAdapter = HeaderAdapter(headerItemAdapter)
        headerAdapter.setOnViewAttachedListener {
            Log.d(TAG, "bannerViewModel.autoScroll()")
            bannerViewModel.autoScroll()
        }
        headerAdapter.setOnViewDetachedListener {
            Log.d(TAG, "bannerViewModel.cancelAutoScroll()")
            bannerViewModel.cancelAutoScroll()
        }
        headerAdapter.setTouchEventListener { motionEvent ->
            Log.d(TAG, "inner touch motionEvent = $motionEvent")
            bannerViewModel.touchViewPager2(motionEvent)
        }


        val stringAdapter = StringAdapter((0..500).map { "string : $it" })

        val concatAdapter =  ConcatAdapter(headerAdapter,stringAdapter)
        // 为RecyclerView配置adapter
        binding.recyclerview.adapter = concatAdapter
        // 设置需要缓存的 ViewHolder数量 防止离屏后再显示时 频繁执行 onBindViewHolder
        binding.recyclerview.setItemViewCacheSize(10)

        binding.recyclerview.setTouchEventListener{
            Log.d(TAG,"inner setTouchEventListener")
            touchViewModel.touchRecyclerview(it)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler){
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bannerViewModel.autoScrollAction.collect {
                    when (it) {
                        is AutoScrollAction.AutoScroll -> {
                            Log.d(TAG, "inner AutoScroll")
                            // 通过设置动画，实现 自动滑动时 平滑滚动
                            headerAdapter.setCurrentItem()
                            Log.d(TAG, "after setCurrentItem position duration 300")
                        }
                    }
                }
            }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e(TAG, "CoroutineExceptionHandler exception : ${exception.message}")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}