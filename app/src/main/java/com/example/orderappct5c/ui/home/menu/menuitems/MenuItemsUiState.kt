package com.example.orderappct5c.ui.home.menu.menuitems

import com.example.orderappct5c.Message
import com.example.orderappct5c.data.menu.MenuItemUi

data class MenuItemsUiState(
    val isLoading : Boolean = false,
    val errorMessage: Message? = null,
    val menuItems : List<MenuItemUi> = emptyList()
)