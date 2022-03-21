package com.borshevskiy.cleanarchitecture.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.borshevskiy.cleanarchitecture.R
import com.borshevskiy.cleanarchitecture.databinding.ActivityShopItemBinding
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.EXTRA_SCREEN_MODE
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.EXTRA_SHOP_ITEM_ID
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.MODE_ADD
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.MODE_UNKNOWN
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.MODE_UPDATE
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.UNDEFINED_ID

class ShopItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopItemBinding
    private lateinit var viewModel: ShopItemViewModel

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.errorInputCount.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            binding.tilCount.error = message
        }
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tilName.error = message
        }
        viewModel.canCloseScreen.observe(this) {
            finish()
        }
    }

    private fun addTextChangeListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_UPDATE && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_UPDATE) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_UPDATE -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this) {
            with(binding) {
                etName.setText(it.name)
                etCount.setText(it.count.toString())
                saveButton.setOnClickListener {
                    viewModel.updateShopItem(etName.text?.toString(),etCount.text?.toString())
                }
            }
        }
    }

    private fun launchAddMode() {
        with(binding) {
            viewModel.addShopItem(etName.text?.toString(),etCount.text?.toString())
        }
    }

    companion object {

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            return intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
        }

        fun newIntentUpdateItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_UPDATE)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}