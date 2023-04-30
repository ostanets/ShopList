package com.example.shoplist.domain

interface ShopListRepository {

    fun createShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun getShopItem(shopItemId: Int): ShopItem

    fun getShopList(): List<ShopItem>

    fun updateShopItem(shopItem: ShopItem)
}