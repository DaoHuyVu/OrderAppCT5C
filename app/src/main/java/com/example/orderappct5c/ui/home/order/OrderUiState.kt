package com.example.orderappct5c.ui.home.order

import com.example.orderappct5c.Message

data class OrderUiState(
    val isLoading : Boolean = false,
    val message : Message? = null,
    val isUserNameValid : Boolean = true,
    val isPhoneNumberValid : Boolean = false,
    val isAddressValid : Boolean = false,
    val isOrderSuccessFully : Boolean = false
)
