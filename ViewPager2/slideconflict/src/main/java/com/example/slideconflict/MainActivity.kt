package com.example.slideconflict

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.forEach
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.slideconflict.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initNavigation()
    }

    private fun initNavigation() {
        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = host.navController

        // 移除 长按 toast
        binding.navigationView.menu.forEach {
            val menuItemView = findViewById<BottomNavigationItemView>(it.itemId)
            menuItemView.setOnLongClickListener(View.OnLongClickListener {
                return@OnLongClickListener true
            })
        }
        // BottomNavigationView 设置 navController
        binding.navigationView.setupWithNavController(navController)

    }
}