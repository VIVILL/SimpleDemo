package com.example.jetpackdemo.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jetpackdemo.fragment.home.viewpager.FirstFragment
import com.example.jetpackdemo.fragment.home.viewpager.SecondFragment
import com.example.jetpackdemo.fragment.home.viewpager.ThirdFragment
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val TAG = "ViewPagerAdapter"
class ViewPagerAdapter @AssistedInject constructor(
    @Assisted("fragmentStringList") private val fragmentStringList: MutableList<String>,
    @Assisted("fm") fm: FragmentManager,
    @Assisted("lifecycle") lifecycle: Lifecycle
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