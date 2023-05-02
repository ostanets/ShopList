package com.example.shoplist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _errorInputTitle = MutableLiveData<Boolean>()
    val errorInputTitle: LiveData<Boolean>
        get() = _errorInputTitle
    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount
    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    fun createShopItem(inputTitle: String?, inputCount: String?) {
        val title = parseTitle(inputTitle)
        val count = parseCount(inputCount)
        val isFieldsValid = validateInput(title, count)
        if (isFieldsValid) {
            val shopItem = ShopItem(title, count, true)
            createShopItemUseCase.createShopItem(shopItem)
            finishWork()
        }
    }

    fun editShopItem(inputTitle: String?, inputCount: String?) {
        val title = parseTitle(inputTitle)
        val count = parseCount(inputCount)
        val isFieldsValid = validateInput(title, count)
        if (isFieldsValid) {
            _shopItem.value?.let {
                val item = it.copy(title = title, count = count)
                updateShopItemUseCase.updateShopItem(item)
                finishWork()
            }
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
            _errorInputTitle.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetInputTitleError() {
        _errorInputTitle.value = false
    }

    fun resetInputCountError() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}