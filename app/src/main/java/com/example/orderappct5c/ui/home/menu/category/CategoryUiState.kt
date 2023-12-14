package com.example.orderappct5c.ui.home.menu.category

data class CategoryUiState(
    val isLoading : Boolean = false,
    val categories : List<Category> = emptyList(),
    val errorMessage : String? = null
)