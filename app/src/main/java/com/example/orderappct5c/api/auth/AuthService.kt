package com.example.orderappct5c.api.auth

import com.example.orderappct5c.data.auth.LoginRequest
import com.example.orderappct5c.data.auth.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @POST("/api/auth/signup")
    @Headers("Accept-Version: v1")
    suspend fun signUp( @Body signUpRequest : SignUpRequest) : Response<AuthResponse>
    @POST("/api/auth/login")
    @Headers("Accept-Version: v1")
    suspend fun login( @Body loginRequest : LoginRequest) : Response<AuthResponse>
}