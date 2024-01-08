package com.example.orderappct5c.ui.home.personal.resetpassword

import com.example.orderappct5c.Message

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val message : Message? = null,
    val isSuccessful : Boolean = false,
    val canReset : Boolean = false
)