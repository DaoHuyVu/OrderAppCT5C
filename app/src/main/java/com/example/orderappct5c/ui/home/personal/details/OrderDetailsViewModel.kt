package com.example.orderappct5c.ui.home.personal.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.Message
import com.example.orderappct5c.api.order.OrderDetailRepository
import com.example.orderappct5c.api.order.OrderDetails
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.ui.home.personal.history.OrderHistoryUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class OrderDetailsViewModel @AssistedInject constructor(
    @Assisted private val orderId : Long,
    private val orderDetailsRepository: OrderDetailRepository
)  : ViewModel(){
    private val _orderDetails = MutableLiveData(OrderDetailsUiState())
    init{
        _orderDetails.value = OrderDetailsUiState(orderDetails = orderDetailsRepository.getOrderDetails(orderId) )
    }
    val orderDetails : LiveData<OrderDetailsUiState> get() = _orderDetails

    fun cancelOrder(){
        handleCallback { orderDetailsRepository.cancelOrder(orderId) }
    }
    private fun handleCallback(callback : suspend () -> ApiResult<OrderDetails> ){
        viewModelScope.launch {
            _orderDetails.value = _orderDetails.value?.copy(isLoading = true)
            when(val result = callback.invoke()){
                is ApiResult.Success -> _orderDetails.value = OrderDetailsUiState(orderDetails = result.data, isSuccessful = true)
                is ApiResult.Failure -> _orderDetails.value = _orderDetails.value?.copy(message = result.message, isLoading = false)
                is ApiResult.Exception -> _orderDetails.value = _orderDetails.value?.copy(message = Message.SERVER_BREAKDOWN, isLoading = false)
            }
        }
    }
    fun messageShown(){
        _orderDetails.value = _orderDetails.value?.copy(message = null)
    }
    @AssistedFactory
    interface OrderDetailsFactory{
        fun create(orderId : Long) : OrderDetailsViewModel
    }
    companion object{
        fun provideFactory(
            orderId : Long,
            factory : OrderDetailsFactory
        ) = object : ViewModelProvider.Factory{
            @Suppress("Unchecked_cast")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(orderId) as T
            }
        }
    }
}