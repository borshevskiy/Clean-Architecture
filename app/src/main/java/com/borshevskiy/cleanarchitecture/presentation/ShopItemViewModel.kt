package com.borshevskiy.cleanarchitecture.presentation

import androidx.lifecycle.ViewModel
import com.borshevskiy.cleanarchitecture.data.ShopListRepositoryImpl
import com.borshevskiy.cleanarchitecture.domain.AddShopItemUseCase
import com.borshevskiy.cleanarchitecture.domain.GetShopItemUseCase
import com.borshevskiy.cleanarchitecture.domain.ShopItem
import com.borshevskiy.cleanarchitecture.domain.UpdateShopItemUseCase
import kotlin.random.Random

class ShopItemViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)

    fun getShopItem(shopItemId: Int) {
        getShopListUseCase.getShopItem(shopItemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            addShopItemUseCase.addShopItem(ShopItem(name,count,true))
        }
    }

    fun updateShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            updateShopItemUseCase.updateShopItem(ShopItem(name,count,true))
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {0}
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            //TODO()
            result = false
        }
        if(count <=0 ) {
            //TODO()
            result = false
        }
        return result
    }




}