package com.example.orderappct5c.ui.home.menu.itemdetail

data class OrderItemDto(
    val id : Long,
    val name : String,
    val price : Double,
    val imageUrl : String,
    val quantity : Int
)