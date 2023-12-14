package com.example.orderappct5c.ui.signup

import com.example.orderappct5c.Message

data class SignUpUiState(
    val isSignUpSuccessfully : Boolean = false,
    val isLoading : Boolean = false,
    val message : Message? = null,

    )
