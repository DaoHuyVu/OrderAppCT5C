package com.example.orderappct5c.api.cart

import com.example.orderappct5c.api.order.StoreDto
import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItem
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CartService {
    @GET("/api/cart")
    suspend fun getCartList() : Response<List<OrderItem>>
    @FormUrlEncoded
    @POST("api/cart")
    suspend fun addItem(@Field("id") id : Long,@Field("quantity") quantity : Int) : Response<OrderItem>
    @FormUrlEncoded
    @PATCH("api/cart/{id}")
    suspend fun patchItem(@Path("id") id : Long,@Field("quantity") quantity : Int) : Response<OrderItem>
    @DELETE("api/cart/{id}")
    suspend fun deleteItem(@Path("id") id : Long) : Response<OrderItem>

}