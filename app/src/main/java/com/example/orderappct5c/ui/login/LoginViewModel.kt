package com.example.orderappct5c.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.ErrorMessage
import com.example.orderappct5c.data.login.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepo
) : ViewModel() {
    private var _userName = ""
    val userName get() = _userName
    private var _password = ""
    val password get() = _password

    private val _loginUiState = MutableLiveData<LoginUiState>()
    val loginUiState : LiveData<LoginUiState> get() = _loginUiState

    fun onUserNameChange(text : String){
        _userName = text
    }
    fun onPasswordChange(text : String){
       _password = text
    }
    fun errorMessageShown(){
        _loginUiState.value = LoginUiState(
            errorMessage = null
        )
    }
    fun login(){
        if(userName.isEmpty()){
            _loginUiState.value = LoginUiState(
                errorMessage = ErrorMessage.USERNAME_EMPTY
            )
        }
        else{
            if(password.isEmpty()){
                _loginUiState.value = LoginUiState(
                    errorMessage = ErrorMessage.PASSWORD_EMPTY
                )
            }
            else{
                viewModelScope.launch {
                    _loginUiState.value = LoginUiState(
                        isLoggingIn = true
                    )
                    delay(2000)
                    try{
                        val loginResponse = loginRepo.login(userName,password)
                        _loginUiState.value = LoginUiState(
                            isUserLoggedIn = true
                        )
                    }catch(exception : Exception){
                        _loginUiState.value = LoginUiState(
                            errorMessage = ErrorMessage.NO_INTERNET_CONNECTION,
                        )
                    }
                }
            }
        }

    }

}