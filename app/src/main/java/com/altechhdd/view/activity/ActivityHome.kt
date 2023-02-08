package com.altechhdd.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.altechhdd.R
import com.altechhdd.databinding.ActivityHomeBinding
import com.altechhdd.viewModel.HomeViewModel
import com.altechhdd.viewModel.SignInViewModel

class ActivityHome : AppCompatActivity() {
    private val homeViewModel by lazy { HomeViewModel(this) }
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this


    }
}