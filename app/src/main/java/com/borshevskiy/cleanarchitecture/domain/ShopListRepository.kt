package com.borshevskiy.cleanarchitecture.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun getShopList(): LiveData<List<ShopItem>>

    fun updateShopItem(shopItem: ShopItem)

    fun getShopItem(shopItemId: Int): ShopItem

}