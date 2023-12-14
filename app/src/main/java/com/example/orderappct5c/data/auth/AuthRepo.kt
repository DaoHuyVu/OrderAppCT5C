package com.example.orderappct5c.data.auth

import com.example.orderappct5c.api.auth.AuthResponse
import com.example.orderappct5c.api.auth.AuthService
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@ViewModelScoped
class AuthRepo @Inject constructor(
    private val authService: AuthService
) {
    suspend fun login(userName: String, password: String): Response<AuthResponse> {
        return authService.login(LoginRequest(userName, password))
    }
    suspend fun signUp(userName: String,email: String,password: String) : Response<AuthResponse>{
        return authService.signUp(SignUpRequest(userName,email,password))
    }
}