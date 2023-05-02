package com.example.shoplist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoplist.R
import com.example.shoplist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {
    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilTitle: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etTitle: TextInputEditText
    private lateinit var etCount: TextInputEditText
    private lateinit var buttonSave: Button

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews()
        defaultSetup()
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this) {
            etTitle.setText(it.title)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            val title = etTitle.text.toString()
            val count = etCount.text.toString()
            viewModel.editShopItem(title, count)
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            val title = etTitle.text.toString()
            val count = etCount.text.toString()
            viewModel.createShopItem(title, count)
        }
    }

    private fun defaultSetup() {
        setupTitle()
        setupCount()
        setupFinish()
    }

    private fun setupFinish() {
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun setupCount() {
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetInputCountError()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
        viewModel.errorInputCount.observe(this) {
            if (it) {
                tilCount.error = R.string.count_error_message.toString()
            } else {
                tilCount.error = null
            }
        }
    }

    private fun setupTitle() {
        etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetInputTitleError()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
        viewModel.errorInputTitle.observe(this) {
            if (it) {
                tilTitle.error = R.string.title_error_message.toString()
            } else {
                tilTitle.error = null
            }
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode

        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
            if (shopItemId < 0) {
                throw RuntimeException("Invalid shop item id $shopItemId")

            }
        }
    }

    private fun initViews() {
        tilTitle = findViewById(R.id.til_title)
        tilCount = findViewById(R.id.til_count)
        etTitle = findViewById(R.id.et_title)
        etCount = findViewById(R.id.et_count)
        buttonSave = findViewById(R.id.save_button)
    }
    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}