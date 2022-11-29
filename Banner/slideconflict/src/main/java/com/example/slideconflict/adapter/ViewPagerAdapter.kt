package com.example.slideconflict.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.slideconflict.fragment.home.viewpager.FirstFragment
import com.example.slideconflict.fragment.home.viewpager.SecondFragment
import com.example.slideconflict.fragment.home.viewpager.ThirdFragment

private const val TAG = "ViewPagerAdapter"
class ViewPagerAdapter(
    private val fragmentStringList: MutableList<String>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return fragmentStringList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (fragmentStringList[position]) {
            "FirstFragment" -> FirstFragment.newInstance("","")
            "SecondFragment" -> SecondFragment.newInstance("","")
            else -> ThirdFragment.newInstance("","")
        }
    }
}