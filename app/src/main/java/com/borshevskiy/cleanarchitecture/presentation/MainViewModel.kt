package com.borshevskiy.cleanarchitecture.presentation

import androidx.lifecycle.ViewModel
import com.borshevskiy.cleanarchitecture.data.ShopListRepositoryImpl
import com.borshevskiy.cleanarchitecture.domain.DeleteShopItemUseCase
import com.borshevskiy.cleanarchitecture.domain.GetShopListUseCase
import com.borshevskiy.cleanarchitecture.domain.ShopItem
import com.borshevskiy.cleanarchitecture.domain.UpdateShopItemUseCase

class MainViewModel(): ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        updateShopItemUseCase.updateShopItem(newItem)
    }
}