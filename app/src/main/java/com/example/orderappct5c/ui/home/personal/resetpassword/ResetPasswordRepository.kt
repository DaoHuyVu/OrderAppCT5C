package com.example.orderappct5c.ui.home.personal.resetpassword

import com.example.orderappct5c.Message
import com.example.orderappct5c.api.auth.AuthService
import com.example.orderappct5c.api.menu.dispatchers.IODispatchers
import com.example.orderappct5c.data.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResetPasswordRepository @Inject constructor(
    private val authService : AuthService,
    @IODispatchers private val ioDispatchers: CoroutineDispatcher
) {
    suspend fun changePassword(oldPassword : String,newPassword : String) : ApiResult<Unit>{
        return withContext(ioDispatchers){
            try{
                val response = authService.changePassword(oldPassword,newPassword)
                if(response.isSuccessful){
                    ApiResult.Success(data = Unit)
                }
                else if (response.code() in 400 until 500)
                    ApiResult.Failure(Message.RESET_FAIL)
                else ApiResult.Failure(Message.SERVER_BREAKDOWN)
            } catch (ex: UnknownHostException) {
                ApiResult.Failure(Message.NO_INTERNET_CONNECTION)
            } catch (ex: Exception) {
                ApiResult.Exception
            }
        }
    }
}