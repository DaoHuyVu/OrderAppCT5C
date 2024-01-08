package com.example.orderappct5c.ui.home.personal.history

import com.example.orderappct5c.Message
import com.example.orderappct5c.api.order.OrderDetailsHistoryView

data class OrderHistoryUiState (
    val isLoading : Boolean = false,
    val message : Message? = null,
    val historyOrderList : List<OrderDetailsHistoryView> = listOf()
)