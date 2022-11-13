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

        val adapter = ViewPagerAdapter(
            fragmentStringList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager2.adapter = adapter
        // 设置 offscreenPageLimit
        // 解决 使用 navigation 时 切换回 viewPager 界面时内存泄漏问题
        binding.viewPager2.offscreenPageLimit = 1
        //绑定tabLayout和viewPager
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "First"
                1 -> tab.text = "Second"
                else -> tab.text = "Third"
            }
        }.attach()

        return binding.root
    }

}