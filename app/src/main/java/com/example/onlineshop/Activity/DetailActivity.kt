package com.example.onlineshop.Activity

import android.os.Bundle
import com.example.onlineshop.model.ItemsModel
import com.example.onlineshop.Helper.ManagmentCart

class DetailActivity : BaseActivity() {

    private lateinit var item: ItemsModel
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        item= intent.getParcelableExtra("object")!!

        managmentCart= ManagmentCart(this)
    }
}