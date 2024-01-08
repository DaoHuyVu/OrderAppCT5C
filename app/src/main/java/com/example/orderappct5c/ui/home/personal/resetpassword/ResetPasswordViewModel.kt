package com.example.orderappct5c.ui.home.personal.resetpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.Message
import com.example.orderappct5c.data.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordRepository: ResetPasswordRepository
) : ViewModel(){
    private val _uiState = MutableLiveData(ResetPasswordUiState())
    val uiState : LiveData<ResetPasswordUiState> get() = _uiState

    var oldPassword = ""
        private set
    var newPassword = ""
        private set
    fun oldPasswordChange(pw : String){
        oldPassword = pw
        if (canResetPassword())
            _uiState.value = _uiState.value?.copy(canReset = true)
        else
            _uiState.value = _uiState.value?.copy(canReset = false)
    }
    fun newPasswordChange(pw : String){
        newPassword = pw
        if (canResetPassword())
            _uiState.value = _uiState.value?.copy(canReset = true)
        else
            _uiState.value = _uiState.value?.copy(canReset = false)
    }
    private fun canResetPassword() : Boolean{
        return isPasswordValid(oldPassword) && isPasswordValid(newPassword)
    }
    fun changePassword(){
        viewModelScope.launch {
            when(val result = resetPasswordRepository.changePassword(oldPassword,newPassword)){
                is ApiResult.Success -> _uiState.value = ResetPasswordUiState(isSuccessful = true)
                is ApiResult.Failure -> _uiState.value = ResetPasswordUiState(message = result.message )
                is ApiResult.Exception -> _uiState.value = ResetPasswordUiState(message = Message.SERVER_BREAKDOWN)
            }
        }
    }
    fun messageShown(){
        _uiState.value = _uiState.value?.copy(message = null)
    }
    private fun isPasswordValid(password : String) : Boolean{
        return password.matches(Regex("[a-zA-Z0-9.^!@#$%&*]{8,}"))
    }
}