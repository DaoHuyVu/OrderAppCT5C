package com.example.orderappct5c.api.order

import com.example.orderappct5c.Message
import com.example.orderappct5c.api.menu.dispatchers.IODispatchers
import com.example.orderappct5c.data.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton
import java.net.UnknownHostException
@Singleton
class OrderDetailRepository @Inject constructor(
    private val orderDetailsService: OrderDetailsService,
    @IODispatchers private val ioDispatchers : CoroutineDispatcher
) {
    private var orderDetailsList = LinkedList<OrderDetails>()
    init{
        CoroutineScope(ioDispatchers).launch {
             fetchOrderDetailList()
        }
    }
    fun getOrderDetails(id : Long) : OrderDetails{
        return orderDetailsList.first{item ->
            item.id == id
        }
    }
    suspend fun cancelOrder(id : Long) : ApiResult<OrderDetails>{
        return try{
            val response = orderDetailsService.cancelOrder(id)
            if(response.isSuccessful){
                val index = orderDetailsList.indexOfFirst { order -> order.id == id }
                orderDetailsList[index] = response.body()!!
                ApiResult.Success(response.body()!!)
            }
            else if(response.code() in 400 until 500)
                ApiResult.Failure(Message.CANCELLED_FAIL)
            else ApiResult.Failure(Message.SERVER_BREAKDOWN)
        }catch(ex : UnknownHostException){
            ApiResult.Failure(Message.NO_INTERNET_CONNECTION)
        }
        catch(ex : Exception){
            ApiResult.Exception
        }
    }
    private suspend fun fetchOrderDetailList(){
        try{
            val response = orderDetailsService.getAllOrder()
            if(response.isSuccessful)
                orderDetailsList = LinkedList(response.body()!!)
        }catch(_ : Exception){ }
    }

    suspend fun fetchOrderDetailHistoryViewList() : ApiResult<List<OrderDetailsHistoryView>>{
        return try{
            val response = orderDetailsService.getAllOrder()
            if(response.isSuccessful){
                orderDetailsList = LinkedList(response.body()!!)
                ApiResult.Success(orderDetailsList.map { item -> item.toOrderDetailHistoryView() })
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

}