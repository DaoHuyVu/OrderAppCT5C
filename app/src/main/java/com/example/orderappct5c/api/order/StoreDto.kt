package com.example.orderappct5c.api.order

data class StoreDto (
    val id : Long,
    val address : Address
){
    override fun toString(): String {
        return address.toString()
    }
}
data class Address(
    val city : String,
    val district : String,
    val street : String
){
    override fun toString(): String {
        return "$street - $district - $city"
    }
}