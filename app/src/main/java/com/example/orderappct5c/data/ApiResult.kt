package com.example.orderappct5c.data

sealed class ApiResult<out T>{
    data class Success<T>(val data : T) : ApiResult<T>()
    data class Failure(val errorResponse : ErrorResponse) : ApiResult<Nothing>()
    data class Exception(val throwable: Throwable) : ApiResult<Nothing>()
}
