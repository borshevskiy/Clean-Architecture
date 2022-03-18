package com.borshevskiy.cleanarchitecture.presentation.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.borshevskiy.cleanarchitecture.databinding.ItemShopDisabledBinding
import com.borshevskiy.cleanarchitecture.databinding.ItemShopEnabledBinding
import com.borshevskiy.cleanarchitecture.domain.ShopItem
import com.borshevskiy.cleanarchitecture.presentation.ShopItemActivity
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.VIEW_TYPE_DISABLED
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.VIEW_TYPE_ENABLED

class ShopListAdapter : ListAdapter<ShopItem, ShopListAdapter.ShopRecyclerViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

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
        return if (getItem(position).enabled) {
             VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    override fun onBindViewHolder(holder: ShopRecyclerViewHolder, position: Int) {
        val shopItem = getItem(position)
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
                        onShopItemClickListener?.invoke(shopItem)
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
                        onShopItemClickListener?.invoke(shopItem)
                    }
                }
            }
        }
    }
}