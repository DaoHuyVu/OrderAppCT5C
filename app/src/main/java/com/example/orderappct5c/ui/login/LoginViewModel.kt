package com.example.orderappct5c.ui.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.Message
import com.example.orderappct5c.data.auth.AuthRepo
import com.example.orderappct5c.util.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: AuthRepo,
    private val dataStoreUtil: DataStoreUtil
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
            message = null
        )
    }
    fun login(){
        if(userName.isEmpty()){
            _loginUiState.value = LoginUiState(
                message = Message.USERNAME_EMPTY
            )
        }
        else{
            if(password.isEmpty()){
                _loginUiState.value = LoginUiState(
                    message = Message.PASSWORD_EMPTY
                )
            }
            else{
                viewModelScope.launch {
                    _loginUiState.value = LoginUiState(
                        isLoggingIn = true
                    )
                    try{
                        val loginResponse = loginRepo.login(userName,password)
                        if(loginResponse.isSuccessful){
                            println(loginResponse.body())
                            dataStoreUtil.saveUserInfo(
                                loginResponse.body()?.accessToken,
                                loginResponse.body()?.userName
                            )
                            _loginUiState.value = LoginUiState(
                                isUserLoggedIn = true
                            )
                        }
                        else{
                            _loginUiState.value = LoginUiState(
                                message = Message.LOGIN_FAIL
                            )
                        }
                    }
                    catch(exception : ConnectException){
                        _loginUiState.value = LoginUiState(
                            message = Message.NO_INTERNET_CONNECTION,
                        )
                    }
                    catch (exception : Exception){
                        _loginUiState.value = LoginUiState(
                            message = Message.SOMETHING_WRONG,
                        )
                    }
                }
            }
        }

    }

}