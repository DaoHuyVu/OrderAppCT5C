package com.example.orderappct5c.api.auth

import com.squareup.moshi.Json

data class AuthResponse(
    val accessToken : String,
    val userName : String,
    val role : List<String>
)
