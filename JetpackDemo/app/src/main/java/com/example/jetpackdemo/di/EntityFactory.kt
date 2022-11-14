package com.example.jetpackdemo.di

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.jetpackdemo.adapter.ViewPagerAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ViewPagerAdapterEntityFactory {
    fun create(
        @Assisted("fragmentStringList") fragmentStringList: MutableList<String>,
        @Assisted("fm") fm: FragmentManager,
        @Assisted("lifecycle") lifecycle: Lifecycle
    ): ViewPagerAdapter
}