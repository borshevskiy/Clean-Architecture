package com.borshevskiy.cleanarchitecture.data

import com.borshevskiy.cleanarchitecture.domain.ShopItem
import com.borshevskiy.cleanarchitecture.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl: ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrement = 0

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrement++
        }
        shopList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }

    override fun updateShopItem(shopItem: ShopItem) {
        deleteShopItem(getShopItem(shopItem.id))
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId } ?:
        throw RuntimeException("Element with $shopItemId not found")
    }
}