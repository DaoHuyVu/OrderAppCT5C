package com.example.orderappct5c.ui.home.order

import com.example.orderappct5c.api.cart.CartService
import com.example.orderappct5c.api.menu.dispatchers.IODispatchers
import com.example.orderappct5c.api.order.OrderDetails
import com.example.orderappct5c.api.order.OrderDetailsService
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.api.order.StoreDto
import com.example.orderappct5c.util.Converter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderDetailsService: OrderDetailsService,
    @IODispatchers private val ioDispatchers: CoroutineDispatcher = Dispatchers.IO
){
    private var store = listOf<StoreDto>()
    init{
        CoroutineScope(ioDispatchers).launch {
            fetchAllStore()
        }
    }
    private suspend fun fetchAllStore() : ApiResult<List<StoreDto>>{
        return try{
            val response = orderDetailsService.getAllStore()
            if(response.isSuccessful){
                store = response.body()!!
                ApiResult.Success(store)
            }
            else ApiResult.Failure(Converter.stringToObject(response.errorBody().toString()))
        }catch(ex : Exception){
            ApiResult.Exception(ex)
        }
    }
    suspend fun getAllStore() : ApiResult<List<StoreDto>>{
        return if(store.isEmpty()) fetchAllStore() else ApiResult.Success(store)

    }
    suspend fun createOrder(map : Map<String,String>) : ApiResult<OrderDetails>{
        return withContext(ioDispatchers){
            try{
                val response = orderDetailsService.createOrder(map)
                if(response.isSuccessful)
                    ApiResult.Success(response.body()!!)
                else ApiResult.Failure(Converter.stringToObject(response.errorBody().toString()))
            }catch (ex : Exception){
                ApiResult.Exception(ex)
            }
        }
    }
}