package com.example.orderappct5c.api.login

import com.example.orderappct5c.data.login.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/api/v1/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>
}