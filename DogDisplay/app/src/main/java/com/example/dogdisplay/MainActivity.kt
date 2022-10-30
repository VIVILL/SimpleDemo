package com.example.dogdisplay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dogdisplay.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binDing by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binDing.root)
    }

}
