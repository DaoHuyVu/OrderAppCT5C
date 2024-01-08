package com.example.orderappct5c.api.auth
data class AuthResponse(
    val accessToken : String,
    val userName : String,
    val role : List<String>
)
