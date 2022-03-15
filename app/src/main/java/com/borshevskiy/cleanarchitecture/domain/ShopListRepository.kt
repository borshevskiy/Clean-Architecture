package com.borshevskiy.cleanarchitecture.domain

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun getShopList(): List<ShopItem>

    fun updateShopItem(shopItem: ShopItem)

    fun getShopItem(shopItemId: Int): ShopItem

}