package com.example.shoplist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R

class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val shopItemTitle: TextView = view.findViewById(R.id.shopItemTitle)
    val shopItemCount: TextView = view.findViewById(R.id.shopItemCount)
}