package com.example.orderappct5c.ui.home.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderappct5c.R
import com.example.orderappct5c.api.order.OrderDetails
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.data.menu.OrderUiState
import com.example.orderappct5c.api.order.StoreDto
import com.example.orderappct5c.ui.home.cart.CartRepository
import com.example.orderappct5c.util.DataStoreUtil
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val dataStoreUtil: DataStoreUtil
) : ViewModel() {
    private val _orderUiState = MutableLiveData(OrderUiState())
    val orderUiState get() = _orderUiState
    private val _branch = MutableLiveData<List<StoreDto>?>()
    init{
        _orderUiState.value = OrderUiState(isLoading = true)
        viewModelScope.launch {
            when(val result = orderRepository.getAllStore()){
                is ApiResult.Success -> {
                    _branch.value = result.data
                    _orderUiState.value = OrderUiState(isLoading = false)
                }
                is ApiResult.Failure -> _orderUiState.value = OrderUiState(message = result.errorResponse.message)
                is ApiResult.Exception -> _orderUiState.value = OrderUiState(message = result.throwable.message)
            }
        }
    }
    val branch get() = _branch
    var selectedStorePosition = 0
    var phoneNumber = ""
    var userName = dataStoreUtil.getUserName()
    var address = ""

    fun userNameChange(un : String){
        userName = un
        validateUserName(un)
    }
    fun phoneNumberChange(pn : String){
        phoneNumber = pn
        validatePhoneNumber(pn)
    }
    fun addressChange(a  : String){
        address = a
        validateAddress(a)
    }
    fun branchChange(position : Int){
        selectedStorePosition = position
    }
    fun messageShown(){
        _orderUiState.value = _orderUiState.value?.copy(message = null)
    }
    private fun validateUserName(un : String) {
        if(un.isNotEmpty() && _orderUiState.value?.isUserNameValid == false)
            _orderUiState.value = _orderUiState.value?.copy(isUserNameValid = true)
        else if(un.isEmpty())
            _orderUiState.value = _orderUiState.value?.copy(isUserNameValid = false)
    }
    private fun validatePhoneNumber(pn : String){
        if(pn.isNotEmpty() && _orderUiState.value?.isPhoneNumberValid == false)
            _orderUiState.value = _orderUiState.value?.copy(isPhoneNumberValid = true)
        else if(pn.isEmpty())
            _orderUiState.value = _orderUiState.value?.copy(isPhoneNumberValid = false)
    }
    private fun validateAddress(a : String){
        if(a.isNotEmpty() && _orderUiState.value?.isAddressValid == false)
            _orderUiState.value = _orderUiState.value?.copy(isAddressValid = true)
        else if(a.isEmpty())
            _orderUiState.value = _orderUiState.value?.copy(isAddressValid = false)
    }
    fun makeOrder(){
        _orderUiState.value = _orderUiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            val gson = Gson()
            val map = hashMapOf<String,String>()
            map["phone"] = phoneNumber
            map["address"] = address
            map["orderItemList"] = gson.toJson(cartRepository.getReadOnlyList())
            map["price"] = gson.toJson(cartRepository.getReadOnlyList().fold(0.0){result,item -> result + item.price*item.quantity})
            map["userName"] = userName!!
            map["storeDto"] = gson.toJson(_branch.value!![selectedStorePosition])

            when(val result = orderRepository.createOrder(map)){
                is ApiResult.Success -> {
                    _orderUiState.value = OrderUiState(message = "Order successfully")
                    clearInput()
                }
                is ApiResult.Failure -> _orderUiState.value = _orderUiState.value?.copy(message = result.errorResponse.message)
                is ApiResult.Exception -> _orderUiState.value = _orderUiState.value?.copy(message = result.throwable.message)
            }
        }
    }
    private fun clearInput(){
        phoneNumber = ""
        address = ""
        selectedStorePosition = 0
    }
}