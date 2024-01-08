package com.example.orderappct5c.ui.home.cart

import com.example.orderappct5c.Message
import com.example.orderappct5c.api.cart.CartService
import com.example.orderappct5c.api.menu.dispatchers.IODispatchers
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItemDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartService: CartService,
    @IODispatchers private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
){
    private val repositoryScope = CoroutineScope(dispatcher)
    private var orderItemDtos = LinkedList<OrderItemDto>()
    init{
        repositoryScope.launch{
            fetchCartList()
        }
    }
    private suspend fun fetchCartList(){
        try{
            val response = cartService.getCartList()
            if(response.isSuccessful){
                orderItemDtos = LinkedList(response.body()!!)
            }
        }catch (_: Exception){ }
    }
    suspend fun addToCart(quantity : Int,id : Long) : ApiResult<OrderItemDto>{
        return withContext(dispatcher){
            try {
                val response = cartService.addItem(id, quantity)
                if (response.isSuccessful || response.body() != null) {
                    val addedItem = response.body()!!
                    // add if not in orderItems otherwise update existing one
                    val index = orderItemDtos.indexOfFirst { item ->
                        item.id == addedItem.id
                    }
                    if (index != -1) {
                        orderItemDtos[index] = addedItem
                    } else {
                        orderItemDtos.add(addedItem)
                    }
                    ApiResult.Success(addedItem)
                }
                else if(response.code() in 400 until 500)
                    ApiResult.Failure(Message.ADDED_FAIL)
                else ApiResult.Failure(Message.SERVER_BREAKDOWN)
            }
            catch(ex : UnknownHostException){
                ApiResult.Failure(Message.NO_INTERNET_CONNECTION)
            }
            catch(ex : Exception){
                ApiResult.Exception
            }
        }
    }
    suspend fun fetchOrderItemList() : ApiResult<List<OrderItemDto>>{
            return try{
                val response = cartService.getCartList()
                if(response.isSuccessful){
                    orderItemDtos = LinkedList(response.body()!!)
                    ApiResult.Success(data = orderItemDtos.toList())
                }else if(response.code() in 400 until 500)
                    ApiResult.Failure(Message.LOAD_ERROR)
                else ApiResult.Failure(Message.SERVER_BREAKDOWN)
            }catch(ex : UnknownHostException){
                ApiResult.Failure(Message.NO_INTERNET_CONNECTION)
            }
            catch(ex : Exception){
                ApiResult.Exception
            }
        }

    fun getList() = orderItemDtos.toList()

    suspend fun modifyQuantity(id : Long,quantity: Int) : ApiResult<List<OrderItemDto>>{
        val response = cartService.patchItem(id,quantity)
        return try{
            if(response.isSuccessful || response.body() != null){
                val index = orderItemDtos.indexOfFirst{ item -> item.id == id}
                orderItemDtos[index] = response.body()!!
                ApiResult.Success(orderItemDtos.toList())
            }
            else if(response.code() in 400 until 500)
                ApiResult.Failure(Message.MODIFIED_FAIL)
            else ApiResult.Failure(Message.SERVER_BREAKDOWN)
        }catch(ex : UnknownHostException){
            ApiResult.Failure(Message.NO_INTERNET_CONNECTION)
        }
        catch(ex : Exception){
            ApiResult.Exception
        }
    }
    suspend fun removeItem(id : Long) : ApiResult<List<OrderItemDto>>{
        val response = cartService.deleteItem(id)
        return try{
            if(response.isSuccessful ){
                orderItemDtos.removeAt(orderItemDtos.indexOfFirst{ item -> item.id == id })
                ApiResult.Success(orderItemDtos.toList())
            }
            else if(response.code() in 400 until 500)
                ApiResult.Failure(Message.REMOVED_FAIL)
            else ApiResult.Failure(Message.SERVER_BREAKDOWN)
        }catch(ex : UnknownHostException){
            ApiResult.Failure(Message.NO_INTERNET_CONNECTION)
        }
        catch(ex : Exception){
            ApiResult.Exception
        }
    }
    fun clearOrder(){
        orderItemDtos.clear()
    }
}