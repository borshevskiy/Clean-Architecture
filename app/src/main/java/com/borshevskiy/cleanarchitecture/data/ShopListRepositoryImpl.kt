package com.borshevskiy.cleanarchitecture.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.borshevskiy.cleanarchitecture.domain.ShopItem
import com.borshevskiy.cleanarchitecture.domain.ShopListRepository
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.UNDEFINED_ID
import kotlin.random.Random

object ShopListRepositoryImpl: ShopListRepository {

    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = sortedSetOf<ShopItem>({o1,o2 -> o1.id.compareTo(o2.id)})
    private var autoIncrement = 0

    init {
        for (i in 0 until 10) {
            addShopItem(ShopItem("Name $i",i,Random.nextBoolean()))
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == UNDEFINED_ID) {
            shopItem.id = autoIncrement++
        }
        shopList.add(shopItem)
        updateLiveData()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateLiveData()
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    override fun updateShopItem(shopItem: ShopItem) {
        deleteShopItem(getShopItem(shopItem.id))
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId } ?:
        throw RuntimeException("Element with $shopItemId not found")
    }

    private fun updateLiveData() {
        shopListLD.value = shopList.toList()
    }
}