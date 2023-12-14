package com.example.orderappct5c.data.menu

data class OrderUiState(
    val isLoading : Boolean = false,
    val message : String? = null,
    val isUserNameValid : Boolean = true,
    val isPhoneNumberValid : Boolean = false,
    val isAddressValid : Boolean = false
)
