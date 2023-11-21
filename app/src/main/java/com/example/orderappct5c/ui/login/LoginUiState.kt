package com.example.orderappct5c.ui.login

import com.example.orderappct5c.ErrorMessage

data class LoginUiState (
    val isLoggingIn : Boolean = false,
    val isUserLoggedIn : Boolean = false,
    val errorMessage : ErrorMessage? = null
)