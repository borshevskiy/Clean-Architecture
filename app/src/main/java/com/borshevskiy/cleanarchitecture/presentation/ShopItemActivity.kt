package com.borshevskiy.cleanarchitecture.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.borshevskiy.cleanarchitecture.databinding.ActivityShopItemBinding
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.EXTRA_SCREEN_MODE
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.EXTRA_SHOP_ITEM_ID
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.MODE_ADD
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.MODE_UPDATE

class ShopItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mode = intent.getStringExtra("extra_mode")
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