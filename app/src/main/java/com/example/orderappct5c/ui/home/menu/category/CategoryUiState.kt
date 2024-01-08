package com.example.orderappct5c.ui.home.menu.category

import com.example.orderappct5c.Message

data class CategoryUiState(
    val isLoading : Boolean = false,
    val categories : List<Category> = emptyList(),
    val errorMessage : Message? = null
)