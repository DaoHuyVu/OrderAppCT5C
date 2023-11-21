package com.example.orderappct5c.ui.signup

import com.example.orderappct5c.ErrorMessage

data class SignUpUiState(
    val isUserSignUpSuccessfully : Boolean = false,
    val isLoading : Boolean = false,
    val errorMessage : ErrorMessage? = null,

)
