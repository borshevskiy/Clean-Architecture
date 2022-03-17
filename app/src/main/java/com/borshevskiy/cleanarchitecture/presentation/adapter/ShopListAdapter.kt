package com.borshevskiy.cleanarchitecture.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.borshevskiy.cleanarchitecture.databinding.ItemShopDisabledBinding
import com.borshevskiy.cleanarchitecture.databinding.ItemShopEnabledBinding
import com.borshevskiy.cleanarchitecture.domain.ShopItem
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.VIEW_TYPE_DISABLED
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.VIEW_TYPE_ENABLED

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopRecyclerViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null

    sealed class ShopRecyclerViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

        class EnabledViewHolder(val binding: ItemShopEnabledBinding): ShopRecyclerViewHolder(binding)

        class DisabledViewHolder(val binding: ItemShopDisabledBinding): ShopRecyclerViewHolder(binding)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopRecyclerViewHolder {
        return when(viewType) {
            VIEW_TYPE_ENABLED -> ShopRecyclerViewHolder.EnabledViewHolder(ItemShopEnabledBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_DISABLED -> ShopRecyclerViewHolder.DisabledViewHolder(ItemShopDisabledBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (shopList[position].enabled) {
             VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    override fun onBindViewHolder(holder: ShopRecyclerViewHolder, position: Int) {
        val shopItem = shopList[position]
        when(holder) {
            is ShopRecyclerViewHolder.EnabledViewHolder -> {
                with(holder.binding) {
                    tvName.text = shopItem.name
                    tvCount.text = shopItem.count.toString()
                    itemShopRowLayout.setOnLongClickListener {
                        onShopItemLongClickListener?.invoke(shopItem)
                        true
                    }
                    itemShopRowLayout.setOnClickListener {
                        Log.d("CLICK", shopItem.toString())
                    }
                }
            }
            is ShopRecyclerViewHolder.DisabledViewHolder -> {
                with(holder.binding) {
                    tvName.text = shopItem.name
                    tvCount.text = shopItem.count.toString()
                    itemShopRowLayout.setOnLongClickListener {
                        onShopItemLongClickListener?.invoke(shopItem)
                        true
                    }
                    itemShopRowLayout.setOnClickListener {
                        Log.d("CLICK", shopItem.toString())
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
    }
}