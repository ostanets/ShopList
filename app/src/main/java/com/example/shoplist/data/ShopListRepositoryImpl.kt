package com.example.shoplist.data

import com.example.shoplist.domain.ShopItem
import com.example.shoplist.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    init {
        for (i in 0 until 10) {
            val item = ShopItem(
                "TestItem $i",
                i,
                true
            )
            createShopItem(item)
        }
    }

    override fun createShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId } ?: throw RuntimeException("Shop item with id $shopItemId is not found")
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }

    override fun updateShopItem(shopItem: ShopItem) {
        val oldShopItem = getShopItem(shopItem.id)
        deleteShopItem(oldShopItem)
        createShopItem(shopItem)
    }
}