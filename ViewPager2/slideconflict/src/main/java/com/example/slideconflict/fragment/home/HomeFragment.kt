package com.example.slideconflict.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.slideconflict.adapter.ViewPagerAdapter
import com.example.slideconflict.databinding.FragmentHomeBinding
import com.example.slideconflict.viewmodel.TouchAction
import com.example.slideconflict.viewmodel.TouchViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlin.math.abs

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TouchViewModel by activityViewModels()
    private var startX = 0
    private var startY = 0

    private var mLayoutMediator: TabLayoutMediator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"inner onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d(TAG,"inner onCreateView")

        val fragmentStringList = arrayListOf<String>(
            "FirstFragment",
            "SecondFragment",
            "ThirdFragment"
        )

        // 使用requireActivity().supportFragmentManager时 旋转后会报错
        // ViewPager2使用的问题 FragmentManager is already executing transactions
        // http://bbs.xiangxueketang.cn/question/1977
        /*
        * 尽量不要用getActivity().getSupportFragmentManager()的方式，
        * 而是getChildFragmentManager()管理
        * */
// https://issuetracker.google.com/issues/154751401
// 解决 使用 navigation + viewPager2 + recyclerview 界面切换时内存泄漏问题 注意点：
// 1.使用viewLifecycleOwner.lifecycle 而不是 lifecycle
// 2. recyclerview的adapter 在onDestroyView 中置 null
        val adapter = ViewPagerAdapter(
            fragmentStringList,
            /*requireActivity().supportFragmentManager*/
            childFragmentManager,
            //lifecycle
            viewLifecycleOwner.lifecycle
        )

        binding.viewPager2.adapter = adapter
        // FragmentStatePagerAdapter使用不当引起的内存泄漏问题
        // https://blog.csdn.net/TE28093163/article/details/122992737
        // 设置 offscreenPageLimit
        binding.viewPager2.offscreenPageLimit = fragmentStringList.size -1
        //绑定 tabLayout 和 viewPager
        mLayoutMediator =  TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "First"
                1 -> tab.text = "Second"
                else -> tab.text = "Third"
            }
        }
        mLayoutMediator?.attach()

        return binding.root
    }

    override fun onDestroyView() {
        Log.d(TAG,"inner onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"inner onDestroy")
        // TabLayout 解绑
        mLayoutMediator?.detach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler){
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recyclerviewTouchAction.collect {
                    when (it) {
                        is TouchAction.Touch -> {
                            Log.d(TAG, "inner Touch")
                            when (it.event.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    Log.d(TAG, "inner MotionEvent.ACTION_DOWN")
                                    startX = it.event.x.toInt()
                                    startY = it.event.y.toInt()
                                    Log.d(TAG,"ACTION_DOWN startX = $startX startY = $startY ")
                                }
                                MotionEvent.ACTION_MOVE -> {
                                    val endX = it.event.x.toInt()
                                    val endY = it.event.y.toInt()
                                    val disX = abs(endX - startX)
                                    val disY = abs(endY - startY)
                                    if (disX < disY) {
                                        binding.viewPager2.isUserInputEnabled = false
                                        Log.d(TAG, "inner ACTION_MOVE binding.viewPager2.isUserInputEnabled = false")

                                    }
                                }
                                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL-> {
                                    Log.d(TAG, "inner MotionEvent.ACTION_UP or ACTION_CANCEL binding.viewPager2.isUserInputEnabled = true")
                                    startX = 0
                                    startY = 0
                                    binding.viewPager2.isUserInputEnabled = true
                                }

                                else -> {}
                            }
                        }

                    }
                }
            }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e(TAG, "CoroutineExceptionHandler exception : ${exception.message}")
    }

}