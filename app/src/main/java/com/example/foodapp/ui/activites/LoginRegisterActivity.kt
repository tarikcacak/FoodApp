package com.example.foodapp.ui.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.databinding.ActivityLoginRegisterBinding

class LoginRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}