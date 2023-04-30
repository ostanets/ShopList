package com.example.shoplist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.data.ShopListRepositoryImpl
import com.example.shoplist.domain.DeleteShopItemUseCase
import com.example.shoplist.domain.GetShopListUseCase
import com.example.shoplist.domain.ShopItem
import com.example.shoplist.domain.UpdateShopItemUseCase

class MainViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    val updateShopItemUseCase = UpdateShopItemUseCase(repository)

    val shopList = MutableLiveData<List<ShopItem>>()

    fun getShopList() {
        val list = getShopListUseCase.getShopList()
        shopList.value = list
    }

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
        getShopList()
    }

    fun toggleEnableStatus(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        getShopList()
    }
}