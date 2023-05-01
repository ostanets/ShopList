package com.example.shoplist.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val shopItemTitle: TextView = view.findViewById(R.id.shopItemTitle)
        val shopItemCount: TextView = view.findViewById(R.id.shopItemCount)
    }

    var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallback(shopList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        val shopItem = shopList[position]
        return if (shopItem.enabled) {
            ENABLED_SHOP_ITEM
        } else {
            DISABLED_SHOP_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            ENABLED_SHOP_ITEM -> R.layout.item_shop_enabled
            DISABLED_SHOP_ITEM -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        viewHolder.shopItemTitle.text = shopItem.title
            .plus(" ")
            .plus(shopItem.enabled.toString())
        viewHolder.shopItemCount.text = shopItem.count.toString()

        viewHolder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }

        viewHolder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    companion object {
        const val ENABLED_SHOP_ITEM = 1
        const val DISABLED_SHOP_ITEM = 2

        const val MAX_POOL_SIZE = 10
    }
}