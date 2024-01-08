package com.example.orderappct5c.api.order

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderDetailsService {
    @POST("api/order")
    @FormUrlEncoded
    suspend fun createOrder(
        @FieldMap map : Map<String,String>
    ) : Response<OrderDetails>
    @GET("/api/store")
    suspend fun getAllStore() : Response<List<StoreDto>>
    @GET("/api/customer/order")
    suspend fun getAllOrder() : Response<List<OrderDetails>>
    @POST("/api/customer/order/{id}")
    suspend fun cancelOrder(@Path("id") id : Long) : Response<OrderDetails>
}