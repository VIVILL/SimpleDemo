package com.example.jetpackdemo.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jetpackdemo.adapter.ViewPagerAdapter
import com.example.jetpackdemo.databinding.FragmentHomeBinding
import com.example.jetpackdemo.di.ViewPagerAdapterEntityFactory
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "HomeFragment"
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var mLayoutMediator: TabLayoutMediator? = null


    @Inject
    lateinit var viewPagerAdapterEntityFactory: ViewPagerAdapterEntityFactory

    lateinit var adapter: ViewPagerAdapter

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

        adapter = viewPagerAdapterEntityFactory.create(
            fragmentStringList,
            requireActivity().supportFragmentManager,
            lifecycle)
        /*val adapter = ViewPagerAdapter(
            fragmentStringList,
            requireActivity().supportFragmentManager,
            lifecycle
        )*/

        binding.viewPager2.adapter = adapter
        // 设置 offscreenPageLimit
        // 解决 使用 navigation 时 切换回 viewPager2 界面时内存泄漏问题
        binding.viewPager2.offscreenPageLimit = 1
        //绑定 tabLayout 和viewPager
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