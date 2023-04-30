package com.example.shoplist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun createShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun getShopItem(shopItemId: Int): ShopItem

    fun getShopList(): LiveData<List<ShopItem>>

    fun updateShopItem(shopItem: ShopItem)
}