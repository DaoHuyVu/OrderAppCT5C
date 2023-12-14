package com.example.orderappct5c.ui.home.cart

import android.app.Application
import android.util.Log
import androidx.preference.Preference
import com.example.orderappct5c.api.cart.CartService
import com.example.orderappct5c.api.menu.dispatchers.IODispatchers
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.data.ErrorResponse
import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItem
import com.example.orderappct5c.util.Converter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartService: CartService,
    @IODispatchers private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
){
    private val repositoryScope = CoroutineScope(dispatcher)
    private var orderItems = LinkedList<OrderItem>()
    init{
        repositoryScope.launch{
            val response = cartService.getCartList()
            orderItems =
                if(response.isSuccessful || response.body() != null) LinkedList(response.body()!!)
                else LinkedList()
        }
    }
    private suspend fun fetchCartList() : ApiResult<LinkedList<OrderItem>>{
        return try{
            val response = cartService.getCartList()
            if(response.isSuccessful){
                if(orderItems.isEmpty()){
                    orderItems = LinkedList(response.body()!!)
                }
                ApiResult.Success(data = orderItems)
            }
            else {
                val errorResponse = Converter.stringToObject<ErrorResponse>(response.errorBody()!!.toString())
                ApiResult.Failure(errorResponse)
            }
        }catch (ex : Exception){
            ApiResult.Exception(ex)
        }
    }
    suspend fun addToCart(quantity : Int,id : Long) : ApiResult<OrderItem>{
        return withContext(dispatcher){
            val response = cartService.addItem(id,quantity)
            if(response.isSuccessful || response.body() != null){
                val addedItem = response.body()!!
                // add if not in orderItems otherwise update existing one
                val index = orderItems.indexOfFirst{
                        item -> item.id == addedItem.id
                }
                if(index != -1){
                    orderItems[index] = addedItem
                }
                else{
                    orderItems.add(addedItem)
                }
                ApiResult.Success(addedItem)
            }
            else ApiResult.Failure(Converter.stringToObject(response.errorBody().toString()))
        }
    }
    suspend fun getCartList() : ApiResult<LinkedList<OrderItem>>{
        return if(orderItems.isEmpty()) fetchCartList() else ApiResult.Success(orderItems)
    }
    fun getReadOnlyList() = orderItems.toList()

    suspend fun modifyQuantity(id : Long,quantity: Int) : ApiResult<LinkedList<OrderItem>>{
        val response = cartService.patchItem(id,quantity)
        return if(response.isSuccessful || response.body() != null){
            val index = orderItems.indexOfFirst{item -> item.id == id}
            orderItems[index] = response.body()!!
            ApiResult.Success(orderItems)
        }
        else ApiResult.Failure(Converter.stringToObject(response.errorBody().toString()))
    }
    suspend fun removeItem(id : Long) : ApiResult<LinkedList<OrderItem>>{
        val response = cartService.deleteItem(id)
        return if(response.isSuccessful ){
            orderItems.removeAt(orderItems.indexOfFirst { item -> item.id == id })
            ApiResult.Success(orderItems)
        }
        else ApiResult.Failure(Converter.stringToObject(response.errorBody().toString()))
    }
}