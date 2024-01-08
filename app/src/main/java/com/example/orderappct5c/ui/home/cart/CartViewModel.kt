package com.example.orderappct5c.ui.home.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.Message
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    private var currentId = 0L;
    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice get() = _totalPrice
    private var job : Job? = null
    private val _cartUiState = MutableLiveData<CartUiState>()
    init{
        _cartUiState.value = CartUiState(isLoading = true)
    }
    val cartUiState get() = _cartUiState

    fun modifyQuantity(id : Long,quantity : Int){
        if(currentId == id){
            job?.cancel()
        }
        else{
            currentId = id
        }
        job = viewModelScope.launch {
            delay(500L)
            _cartUiState.value = _cartUiState.value?.copy(isLoading = true)
            handleCallback { cartRepository.modifyQuantity(id,quantity) }
        }
        job?.start()
    }

    fun removeItem(id : Long){
        _cartUiState.value = _cartUiState.value?.copy(isLoading = true)
        handleCallback { cartRepository.removeItem(id) }
    }
    fun messageShown(){
        _cartUiState.value = _cartUiState.value?.copy(message = null)
    }
     fun getList(){
         handleCallback {cartRepository.fetchOrderItemList()}
     }
    private fun handleCallback(callback : suspend () -> ApiResult<List<OrderItemDto>>){
        viewModelScope.launch {
            when(val result = callback.invoke()){
                is ApiResult.Success -> _cartUiState.value = _cartUiState.value?.copy(cartList = result.data, isLoading = false)
                is ApiResult.Failure -> _cartUiState.value = CartUiState(message = result.message)
                is ApiResult.Exception -> _cartUiState.value = CartUiState(message = Message.SERVER_BREAKDOWN)
            }
            totalPrice.value = _cartUiState.value?.cartList?.let{list ->
                list.map { item -> item.price*item.quantity }.fold(0.0){
                        result,element -> result + element
                }
            }
        }
    }
}