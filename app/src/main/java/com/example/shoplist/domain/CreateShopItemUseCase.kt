package com.example.shoplist.domain

class CreateShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun createShopItem(shopItem: ShopItem) {
        shopListRepository.createShopItem(shopItem)
    }
}