package com.example.orderappct5c.ui.home.order

import com.example.orderappct5c.Message
import com.example.orderappct5c.api.menu.dispatchers.IODispatchers
import com.example.orderappct5c.api.order.OrderDetails
import com.example.orderappct5c.api.order.OrderDetailsService
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.api.order.StoreDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
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
            else if(response.code() in 400 until 500)
                ApiResult.Failure(Message.LOAD_ERROR)
            else ApiResult.Failure(Message.SERVER_BREAKDOWN)
        }catch(ex : UnknownHostException){
            ApiResult.Failure(Message.NO_INTERNET_CONNECTION)
        }
        catch(ex : Exception){
            ApiResult.Exception
        }
    }
    suspend fun getAllStore() : ApiResult<List<StoreDto>>{
        return if(store.isEmpty()) fetchAllStore() else ApiResult.Success(store)

    }
    suspend fun createOrder(map : Map<String,String>) : ApiResult<OrderDetails>{
        return withContext(ioDispatchers) {
            try {
                val response = orderDetailsService.createOrder(map)
                if (response.isSuccessful)
                    ApiResult.Success(response.body()!!)
                else if (response.code() in 400 until 500)
                    ApiResult.Failure(Message.ORDER_FAIL)
                else ApiResult.Failure(Message.SERVER_BREAKDOWN)
            } catch (ex: UnknownHostException) {
                ApiResult.Failure(Message.NO_INTERNET_CONNECTION)
            } catch (ex: Exception) {
                ApiResult.Exception
            }
        }
    }
}