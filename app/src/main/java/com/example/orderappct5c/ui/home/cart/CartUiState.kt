package com.example.orderappct5c.ui.home.cart

import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItem
import java.util.LinkedList

data class CartUiState(
    val isLoading : Boolean = false,
    val cartList : LinkedList<OrderItem>? = null,
    val message : String? = null
)
