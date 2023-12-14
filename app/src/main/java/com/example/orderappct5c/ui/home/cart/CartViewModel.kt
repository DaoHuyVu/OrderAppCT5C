package com.example.orderappct5c.ui.home.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.LinkedList
import javax.inject.Inject
@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice get() = _totalPrice
    private var job : Job? = null
    private val _cartUiState = MutableLiveData<CartUiState>()
    init{
        _cartUiState.value = CartUiState(isLoading = true)
        getList()
    }
    val cartUiState get() = _cartUiState
    fun modifyQuantity(id : Long,quantity : Int){
        job?.cancel()
        job = viewModelScope.launch {
            delay(500L)
            _cartUiState.value = CartUiState(isLoading = true)
            handleCallback { cartRepository.modifyQuantity(id,quantity) }
        }
        job?.start()
    }

    fun removeItem(id : Long){
        _cartUiState.value = CartUiState(isLoading = true)
        handleCallback { cartRepository.removeItem(id) }
    }
    fun errorMessageShown(){
        _cartUiState.value = _cartUiState.value?.copy(message = null)
    }
     private fun getList(){
         handleCallback {
             cartRepository.getCartList()
         }
     }
    private fun handleCallback(callback : suspend () -> ApiResult<LinkedList<OrderItem>>){
        viewModelScope.launch {
            when(val result = callback.invoke()){
                is ApiResult.Success -> {
                    _cartUiState.value = CartUiState(cartList = result.data)
                    totalPrice.value = _cartUiState.value?.cartList?.let{list ->
                        list.map { item -> item.price*item.quantity }.fold(0.0){
                                result,element -> result + element
                        }
                    }
                }
                is ApiResult.Failure -> _cartUiState.value = CartUiState(message = result.errorResponse.message)
                is ApiResult.Exception -> _cartUiState.value = CartUiState(message = result.throwable.message)
            }
        }
    }
}