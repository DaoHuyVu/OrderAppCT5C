package com.example.orderappct5c.api.order

import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItemDto
import com.google.gson.annotations.SerializedName

data class OrderDetails(
    val id  : Long,
    val phone : String,
    val address : String,
    @SerializedName("orderItemDtoList")
    val orderItemDtoList : List<OrderItemDto>,
    val price : Double,
    val name : String,
    val createdAt : String,
    val status : String,
    val location : Address
){
    fun toOrderDetailHistoryView() = OrderDetailsHistoryView(id,address,price,createdAt,status,location)
}
