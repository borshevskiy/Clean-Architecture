package com.borshevskiy.cleanarchitecture.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.borshevskiy.cleanarchitecture.databinding.ActivityMainBinding
import com.borshevskiy.cleanarchitecture.presentation.adapter.ShopListAdapter
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.MAX_POOL_SIZE
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.VIEW_TYPE_DISABLED
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.VIEW_TYPE_ENABLED

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val mAdapter by lazy { ShopListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            mAdapter.shopList = it
        }
    }

    private fun setupRecyclerView() {
        binding.rvShopList.apply {
            adapter = mAdapter
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_ENABLED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_DISABLED, MAX_POOL_SIZE)
        }
    }
}