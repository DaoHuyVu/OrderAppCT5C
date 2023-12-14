package com.example.orderappct5c.api.order

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderDetailsService {
    @POST("api/order")
    @FormUrlEncoded
    suspend fun createOrder(
        @FieldMap map : Map<String,String>
    ) : Response<OrderDetails>
    @GET("/api/store")
    suspend fun getAllStore() : Response<List<StoreDto>>
}