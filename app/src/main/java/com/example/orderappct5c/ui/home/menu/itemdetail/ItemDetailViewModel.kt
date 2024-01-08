package com.example.orderappct5c.ui.home.menu.itemdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.Message
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.data.menu.MenuRepository
import com.example.orderappct5c.ui.home.cart.CartRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ItemDetailViewModel @AssistedInject constructor(
    @Assisted val itemId :  Long,
    private val menuRepository : MenuRepository,
    private val cartRepository: CartRepository
) : ViewModel(){
    private var _itemDetailUiState = MutableLiveData<ItemDetailUiState>()
    init{
        _itemDetailUiState.value = ItemDetailUiState(isLoading = true)
    }
    val itemDetailUiState get() = _itemDetailUiState
    private val _item = MutableLiveData(menuRepository.getItemDetail(itemId))
    init{
        _itemDetailUiState.value = ItemDetailUiState(price = _item.value!!.price)
    }
    val item get() = _item
    fun addItem(){
        val currentQuantity = _itemDetailUiState.value?.quantity!!
        if(currentQuantity < 100){
            val quantity = currentQuantity.inc()
            val price = quantity*item.value!!.price
            _itemDetailUiState.value = _itemDetailUiState.value?.copy(quantity = quantity,price = price)
        }
    }
    fun removeItem(){
        val currentQuantity = _itemDetailUiState.value?.quantity!!
        if(currentQuantity > 1){
            val quantity = currentQuantity - 1
            val price = quantity*item.value!!.price
            _itemDetailUiState.value = _itemDetailUiState.value?.copy(quantity = quantity,price = price)
        }
    }
    fun addToCart(){
        _itemDetailUiState.value = _itemDetailUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = cartRepository.addToCart(itemDetailUiState.value!!.quantity,itemId)){
                is ApiResult.Success -> _itemDetailUiState.value = _itemDetailUiState.value?.copy(isLoading = false,message = Message.ADD_SUCCESSFULLY)
                is ApiResult.Failure -> _itemDetailUiState.value = _itemDetailUiState.value?.copy(isLoading = false,message = result.message)
                is ApiResult.Exception -> _itemDetailUiState.value = _itemDetailUiState.value?.copy(isLoading = false,message = Message.SERVER_BREAKDOWN)
            }
        }
    }
    fun errorMessageShown(){
        _itemDetailUiState.value = _itemDetailUiState.value?.copy( message = null)
    }

    @AssistedFactory
    interface ItemDetailViewModelFactory{
        fun create(itemId: Long) : ItemDetailViewModel
    }
    companion object{
        fun provideItemDetailFactory(
            factory: ItemDetailViewModelFactory,
            itemId : Long) = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("Unchecked_cast")
                return factory.create(itemId) as T
            }
        }
    }
}