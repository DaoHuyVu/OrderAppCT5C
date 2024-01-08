package com.example.orderappct5c.api.order

data class OrderDetailsHistoryView(
    val id : Long,
    val address : String,
    val price : Double,
    val createdAt : String,
    val status : String,
    val location : Address
)
