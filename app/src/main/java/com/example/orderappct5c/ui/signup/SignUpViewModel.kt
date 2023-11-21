package com.example.orderappct5c.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.ErrorMessage
import com.example.orderappct5c.data.login.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val loginRepo: LoginRepo
) : ViewModel() {
    var username = ""
    var password = ""
    var email = ""
    private val _signUpUiState = MutableLiveData<SignUpUiState>()
    val signUpUiState : LiveData<SignUpUiState> get() = _signUpUiState
    private val _signUpUiFormState = MutableLiveData<SignUpUiFormState>()
    val signUpUiFormState : LiveData<SignUpUiFormState> get() = _signUpUiFormState
    fun userNameChange(userName : String){
        username = userName
        if(isUserNameValid(username) && _signUpUiFormState.value?.userNameInvalid == true)
            _signUpUiFormState.value = SignUpUiFormState(userNameInvalid = false)
        else if(!isUserNameValid(username) && _signUpUiFormState.value?.userNameInvalid == false)
            _signUpUiFormState.value = SignUpUiFormState(userNameInvalid = true)
    }
    fun passwordChange(pw : String){
        password = pw
        if(isPasswordValid(password) && _signUpUiFormState.value?.passwordInvalid == true)
            _signUpUiFormState.value = SignUpUiFormState(passwordInvalid = false)
        else if(!isPasswordValid(password) && _signUpUiFormState.value?.passwordInvalid == false)
            _signUpUiFormState.value = SignUpUiFormState(passwordInvalid = true)
    }
    fun emailChange(em : String){
        email = em
        if(isEmailValid(email) && _signUpUiFormState.value?.emailInvalid == true)
            _signUpUiFormState.value = SignUpUiFormState(emailInvalid = false)
        else if(!isEmailValid(email) && _signUpUiFormState.value?.emailInvalid == false)
            _signUpUiFormState.value = SignUpUiFormState(emailInvalid = true)
    }
    fun signUp(){
        viewModelScope.launch {
            _signUpUiState.value = SignUpUiState(isLoading = true)
            delay(2000L)
            try{

            }
            catch(ex : Exception){
                _signUpUiState.value = SignUpUiState(errorMessage = ErrorMessage.NO_INTERNET_CONNECTION)
            }
        }
    }
    fun errorMessageShown(){
        _signUpUiState.value = SignUpUiState(errorMessage = null)
    }
    private fun isEmailValid(email : String) : Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isUserNameValid(userName: String) : Boolean{
        return userName.length in 8 .. 25
    }
    private fun isPasswordValid(password : String) : Boolean{
        return password.contains("[a-z]+") &&
                password.contains("[A-Z]+") &&
                password.contains("[0-9]+") &&
                password.contains("[.^!@#$%&*]+") &&
                password.length >= 8
    }
}