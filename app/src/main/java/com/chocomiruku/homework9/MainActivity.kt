package com.chocomiruku.homework9

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chocomiruku.homework9.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}