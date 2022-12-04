package com.example.viewpager2.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.viewpager2.adapter.ViewPagerAdapter
import com.example.viewpager2.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"inner onDestroy")
        // TabLayout 解绑
        mLayoutMediator?.detach()
    }
}