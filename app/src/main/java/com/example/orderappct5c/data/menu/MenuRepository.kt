package com.example.orderappct5c.data.menu

import com.example.orderappct5c.api.menu.MenuService
import com.example.orderappct5c.data.ApiResult
import com.example.orderappct5c.data.ErrorResponse
import com.example.orderappct5c.ui.home.menu.category.Category
import com.example.orderappct5c.util.Converter
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
           else{
               val errorResponse = Converter.stringToObject<ErrorResponse>(response.errorBody()!!.string())
               ApiResult.Failure(errorResponse)
           }
       }catch(throwable : Throwable){
                ApiResult.Exception(throwable)
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
            else{
                val errorResponse = Converter.stringToObject<ErrorResponse>(response.errorBody()!!.string())
                ApiResult.Failure(errorResponse)
            }
        }catch(throwable : Throwable){
            ApiResult.Exception(throwable)
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