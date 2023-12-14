package com.example.orderappct5c.api.order

import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItem
import java.time.LocalDateTime

data class OrderDetails(
    val phone : String,
    val address : String,
    val orderItemList : List<OrderItem>,
    val price : Double,
    val userName : String,
    val storeDto: StoreDto,
    val createdAt : LocalDateTime,
    val status : String,
)
