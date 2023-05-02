package com.example.shoplist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupAddShopItemButton()
        setupRecycleView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
    }

    private fun setupRecycleView() {
        val rvShopList = findViewById<RecyclerView>(R.id.shopListRecycleView)
        shopListAdapter = ShopListAdapter()
        with(rvShopList) {
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ENABLED_SHOP_ITEM,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.DISABLED_SHOP_ITEM,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupShopItemLongClickListener()
        setupShopItemClickListener()
        setupShopItemSwipeListener(rvShopList)
    }

    private fun setupShopItemSwipeListener(rvShopList: RecyclerView?) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteShopItem(shopListAdapter.currentList[viewHolder.adapterPosition])
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupShopItemClickListener() {
        shopListAdapter.onShopItemClickListener = {
            val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun setupShopItemLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.toggleEnableStatus(it)
        }
    }

    private fun setupAddShopItemButton() {
        val button = findViewById<FloatingActionButton>(R.id.add_shop_item_button)
        button.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }
    }
}