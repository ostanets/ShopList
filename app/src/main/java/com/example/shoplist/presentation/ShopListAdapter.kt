package com.example.shoplist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    companion object {
        const val ENABLED_SHOP_ITEM = 1
        const val DISABLED_SHOP_ITEM = 2

        const val MAX_POOL_SIZE = 10
    }

    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val shopItemTitle: TextView = view.findViewById(R.id.shopItemTitle)
        val shopItemCount: TextView = view.findViewById(R.id.shopItemCount)
    }

    private var counter = 0
    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        Log.d("ShopListAdapter", "Created views count = ${++counter}")
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        viewHolder.shopItemTitle.text = shopItem.title
            .plus(" ")
            .plus(shopItem.enabled.toString())
        viewHolder.shopItemCount.text = shopItem.count.toString()
        viewHolder.view.setOnLongClickListener {
            true
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
    }
}