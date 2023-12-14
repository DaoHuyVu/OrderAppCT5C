package com.example.orderappct5c.api.menu

import com.example.orderappct5c.ui.home.menu.category.Category
import com.example.orderappct5c.data.menu.MenuItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MenuService {
    @GET("/api/category")
    suspend fun getCategory() : Response<List<Category>>
    @GET("/api/menu")
    suspend fun getMenu(@Query("category") category : String? = null) : Response<List<MenuItem>>
    @GET("/api/menu/{id}")
    suspend fun getMenuItem(@Path("id") id : Long) : Response<MenuItem>

}