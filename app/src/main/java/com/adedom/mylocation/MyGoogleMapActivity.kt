package com.adedom.mylocation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adedom.mylocation.databinding.ActivityMyGoogleMapBinding

class MyGoogleMapActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMyGoogleMapBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}