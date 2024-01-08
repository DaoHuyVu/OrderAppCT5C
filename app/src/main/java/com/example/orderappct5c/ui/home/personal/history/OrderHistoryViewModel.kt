package com.example.orderappct5c.ui.home.personal.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.Message
import com.example.orderappct5c.api.order.OrderDetailRepository
import com.example.orderappct5c.api.order.OrderDetailsHistoryView
import com.example.orderappct5c.data.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderDetailRepository: OrderDetailRepository
) : ViewModel() {
    private val _orderHistoryUiState = MutableLiveData(OrderHistoryUiState())
    init{
        handleCallback { orderDetailRepository.fetchOrderDetailHistoryViewList() }
    }
    val orderHistoryUiState get() = _orderHistoryUiState

    fun refreshOrderHistory(){
        _orderHistoryUiState.value = _orderHistoryUiState.value?.copy(isLoading = true)
        handleCallback { orderDetailRepository.fetchOrderDetailHistoryViewList() }
    }
    fun messageShown(){
        _orderHistoryUiState.value = _orderHistoryUiState.value?.copy(message = null)
    }
    private fun handleCallback(callback : suspend () -> ApiResult<List<OrderDetailsHistoryView>>){
        _orderHistoryUiState.value = _orderHistoryUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = callback.invoke()){
                is ApiResult.Success -> _orderHistoryUiState.value = OrderHistoryUiState(historyOrderList = result.data)
                is ApiResult.Failure -> _orderHistoryUiState.value = _orderHistoryUiState.value?.copy(message = result.message, isLoading = false)
                is ApiResult.Exception -> _orderHistoryUiState.value = _orderHistoryUiState.value?.copy(message = Message.SERVER_BREAKDOWN, isLoading = false)
            }
        }
    }
}