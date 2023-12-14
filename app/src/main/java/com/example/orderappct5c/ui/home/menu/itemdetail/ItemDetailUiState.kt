package com.example.orderappct5c.ui.home.menu.itemdetail

import com.example.orderappct5c.Message

data class ItemDetailUiState(
    val isLoading : Boolean = false,
    val quantity : Int = 1,
    val price : Double = 0.0,
    val message : Message? = null
)
