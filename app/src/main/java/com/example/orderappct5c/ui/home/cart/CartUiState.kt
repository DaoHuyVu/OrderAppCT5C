package com.example.orderappct5c.ui.home.cart

import com.example.orderappct5c.Message
import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItemDto

data class CartUiState(
    val isLoading : Boolean = false,
    val cartList : List<OrderItemDto> = listOf(),
    val message : Message? = null
)
