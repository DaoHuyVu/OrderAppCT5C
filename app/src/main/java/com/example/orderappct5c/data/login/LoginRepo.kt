package com.example.orderappct5c.data.login

import com.example.orderappct5c.api.login.LoginResponse
import com.example.orderappct5c.api.login.LoginService
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@ViewModelScoped
class LoginRepo @Inject constructor(
    private val loginService: LoginService
) {
    suspend fun login(userName : String,password : String) : Response<LoginResponse>{
        return loginService.login(LoginRequest(userName, password))
    }
}