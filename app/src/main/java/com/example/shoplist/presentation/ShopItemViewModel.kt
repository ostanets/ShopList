package com.example.shoplist.presentation

import androidx.lifecycle.ViewModel
import com.example.shoplist.data.ShopListRepositoryImpl
import com.example.shoplist.domain.CreateShopItemUseCase
import com.example.shoplist.domain.GetShopItemUseCase
import com.example.shoplist.domain.ShopItem
import com.example.shoplist.domain.UpdateShopItemUseCase

class ShopItemViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val createShopItemUseCase = CreateShopItemUseCase(repository)
    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)


    fun getShopItem(shopItemId: Int): ShopItem {
        return getShopItemUseCase.getShopItem(shopItemId)
    }

    fun createShopItem(inputTitle: String?, inputCount: String?) {
        val title = parseTitle(inputTitle)
        val count = parseCount(inputCount)
        val isFieldsValid = validateInput(title, count)
        if (isFieldsValid) {
            val shopItem = ShopItem(title, count, true)
            createShopItemUseCase.createShopItem(shopItem)
        }
    }

    fun editShopItem(inputTitle: String?, inputCount: String?) {
        val title = parseTitle(inputTitle)
        val count = parseCount(inputCount)
        val isFieldsValid = validateInput(title, count)
        if (isFieldsValid) {
            val shopItem = ShopItem(title, count, true)
            updateShopItemUseCase.updateShopItem(shopItem)
        }
    }

    private fun parseTitle(title: String?): String {
        return title?.trim() ?: ""
    }

    private fun parseCount(count: String?): Int {
        return count?.trim()?.toIntOrNull() ?: 0
    }

    private fun validateInput(title: String, count: Int): Boolean {
        var result = true
        if (title.isBlank()) {
            //TODO: Error validation title message
            result = false
        }
        if (count <= 0) {
            //TODO: Error validation count message
            result = false
        }
        return result
    }
}