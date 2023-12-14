package com.example.orderappct5c.ui.home.menu.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.data.menu.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val menuRepository: MenuRepository
) : ViewModel(){
    private val _categoryUiState = MutableLiveData(CategoryUiState())
    init{
        getCategory()
    }
    val categoryUiState : LiveData<CategoryUiState> get() = _categoryUiState

     private fun getCategory(){
        viewModelScope.launch{
            _categoryUiState.value = CategoryUiState(isLoading = true)
            when(val categories = menuRepository.getCategory()){
                is ApiResult.Success
                -> _categoryUiState.value = CategoryUiState(categories = categories.data)
                is ApiResult.Failure
                -> _categoryUiState.value = CategoryUiState(errorMessage = categories.errorResponse.message)
                is ApiResult.Exception
                -> _categoryUiState.value = CategoryUiState(errorMessage = categories.throwable.message)
            }
        }
    }
    fun errorMessageShown(){
        _categoryUiState.value = _categoryUiState.value?.copy(errorMessage = null)
    }
}