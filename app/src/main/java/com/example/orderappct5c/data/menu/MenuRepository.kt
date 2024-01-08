package com.example.orderappct5c.data.menu

import com.example.orderappct5c.Message
import com.example.orderappct5c.api.menu.MenuService
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.ui.home.menu.category.Category
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuRepository @Inject constructor(
    private val menuService: MenuService
) {
    private var previousCategoryQuery = ""
    private var _menu : List<MenuItem> = emptyList()
    private val menuUi get() = _menu.map { menuItem -> menuItem.toMenuItemUI() }
    private var _category : List<Category> = emptyList()


    private suspend fun fetchMenu(category : String) : ApiResult<List<MenuItemUi>> {
       return try{
           val response = menuService.getMenu(category)
           if(response.isSuccessful ||  response.body() != null){
               _menu = response.body() ?: emptyList()
               ApiResult.Success(menuUi)
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

    suspend fun getMenuUI(category: String) : ApiResult<List<MenuItemUi>> {
        if(_menu.isEmpty() || previousCategoryQuery != category){
            return fetchMenu(category)
        }
        return ApiResult.Success(menuUi)
    }

    private suspend fun fetchCategory() : ApiResult<List<Category>> {
        return try{
            val response = menuService.getCategory()
            if(response.isSuccessful || response.body() != null){
                if(_category.isEmpty())
                    _category = response.body() ?: emptyList()
                ApiResult.Success(_category)
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

    suspend fun getCategory() : ApiResult<List<Category>> {
        if(_category.isEmpty() ){
            return fetchCategory()
        }
        return ApiResult.Success(_category)
    }
    fun getItemDetail(itemId : Long) : MenuItem{
        return _menu.first{ item -> item.id == itemId }
    }
}