package com.example.orderappct5c.ui.signup

data class SignUpUiFormState(
    val userNameInvalid : Boolean = true,
    val passwordInvalid : Boolean = true,
    val emailInvalid : Boolean = true
)