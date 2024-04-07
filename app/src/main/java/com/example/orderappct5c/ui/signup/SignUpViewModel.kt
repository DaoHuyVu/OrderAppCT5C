package com.example.orderappct5c.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.Message
import com.example.orderappct5c.data.auth.AuthRepo
import com.example.orderappct5c.util.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo : AuthRepo,
    private val dataStoreUtil: DataStoreUtil
) : ViewModel() {
    var username = ""
    var password = ""
    var email = ""
    private val _signUpUiState = MutableLiveData(SignUpUiState())
    val signUpUiState : LiveData<SignUpUiState> get() = _signUpUiState
    private val _signUpUiFormState = MutableLiveData(SignUpUiFormState())
    val signUpUiFormState : LiveData<SignUpUiFormState> get() = _signUpUiFormState
    fun userNameChange(userName : String){
        username = userName
        if(isUserNameValid(username) && _signUpUiFormState.value?.userNameInvalid == true)
            _signUpUiFormState.value = _signUpUiFormState.value?.copy(userNameInvalid = false)
        else if(!isUserNameValid(username) && _signUpUiFormState.value?.userNameInvalid == false)
            _signUpUiFormState.value = _signUpUiFormState.value?.copy(userNameInvalid = true)

    }
    fun passwordChange(pw : String){
        password = pw
        if(isPasswordValid(password) && _signUpUiFormState.value?.passwordInvalid == true)
            _signUpUiFormState.value = _signUpUiFormState.value?.copy(passwordInvalid = false)
        else if(!isPasswordValid(password) && _signUpUiFormState.value?.passwordInvalid == false)
            _signUpUiFormState.value = _signUpUiFormState.value?.copy(passwordInvalid = true)
    }
    fun emailChange(em : String){
        email = em
        if(isEmailValid(email) && _signUpUiFormState.value?.emailInvalid == true)
            _signUpUiFormState.value = _signUpUiFormState.value?.copy(emailInvalid = false)
        else if(!isEmailValid(email) && _signUpUiFormState.value?.emailInvalid == false)
            _signUpUiFormState.value = _signUpUiFormState.value?.copy(emailInvalid = true)
    }
    fun signUp(){
        viewModelScope.launch {
            _signUpUiState.value = SignUpUiState(isLoading = true)
            try{
                val response = authRepo.signUp(username,email,password)
                if(response.isSuccessful){
                    dataStoreUtil.saveUserInfo(
                        response.body()?.accessToken,
                        response.body()?.userName
                    )
                    _signUpUiState.value = SignUpUiState(isSignUpSuccessfully = true)
                }
                else{

                    _signUpUiState.value = SignUpUiState(message = Message.SIGN_UP_FAIL)
                }
            }
            catch(ex : ConnectException){
                _signUpUiState.value = SignUpUiState(message = Message.NO_INTERNET_CONNECTION)
            }catch(ex : Exception){
                _signUpUiState.value = SignUpUiState(message = Message.SERVER_BREAKDOWN)
            }
        }
    }
    fun errorMessageShown(){
        _signUpUiState.value = SignUpUiState(message = null)
    }
    private fun isEmailValid(email : String) : Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isUserNameValid(userName: String) : Boolean{
        return userName.length in 8 .. 25
    }
    private fun isPasswordValid(password : String) : Boolean{
        return password.matches(Regex("^[A-Z][a-z0-9.^!@#$%&*]{7,24}"))
    }
}