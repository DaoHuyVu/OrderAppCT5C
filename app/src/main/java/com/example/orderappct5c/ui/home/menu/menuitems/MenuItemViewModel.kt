package com.example.orderappct5c.ui.home.menu.menuitems

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.data.menu.MenuRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


class MenuItemViewModel @AssistedInject constructor(
    @Assisted private val category : String,
    private val menuRepository: MenuRepository,
) : ViewModel(){
    private val _menuItemsUiState = MutableLiveData(MenuItemsUiState())
    init{
        getMenuItemsUI()
    }
    val menuItemsUiState : LiveData<MenuItemsUiState> get() = _menuItemsUiState

    private fun getMenuItemsUI(){
        viewModelScope.launch {
            _menuItemsUiState.value = MenuItemsUiState(isLoading = true)
            when(val menuItems = menuRepository.getMenuUI(category)){
                is ApiResult.Success -> _menuItemsUiState.value = MenuItemsUiState(menuItems = menuItems.data)
                is ApiResult.Failure -> _menuItemsUiState.value = MenuItemsUiState(errorMessage = menuItems.errorResponse.message)
                is ApiResult.Exception -> _menuItemsUiState.value = MenuItemsUiState(errorMessage = menuItems.throwable.message)
            }
        }
    }
    fun errorMessageShown(){
        _menuItemsUiState.value = _menuItemsUiState.value?.copy(errorMessage = null)
    }
    @AssistedFactory
    interface MenuItemViewModelFactory{
        fun create(category : String) : MenuItemViewModel
    }
    companion object{
        fun provideMenuItemViewModelFactory(
            factory: MenuItemViewModelFactory,
            category : String
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("Unchecked_cast")
                    return factory.create(category) as T
                }
            }
        }
    }
}
