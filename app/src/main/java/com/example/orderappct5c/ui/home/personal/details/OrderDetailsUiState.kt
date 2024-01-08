package com.example.orderappct5c.ui.home.personal.details

import com.example.orderappct5c.Message
import com.example.orderappct5c.api.order.OrderDetails

data class OrderDetailsUiState(
    val isLoading : Boolean = false,
    val message : Message? = null,
    val orderDetails : OrderDetails? = null,
    val isSuccessful : Boolean = false
)
