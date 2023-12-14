package com.example.orderappct5c.data.menu


data class MenuItem(
    val id : Long,
    val name : String,
    val imageUrl : String,
    val price : Double,
    val description : String,
){
    fun toMenuItemUI() : MenuItemUi {
        return MenuItemUi(id,name,imageUrl,price)
    }
}

