package com.example.orderappct5c.data

import com.example.orderappct5c.Message

sealed class ApiResult<out T>{
    data class Success<T>(val data : T) : ApiResult<T>()
    data class Failure(val message : Message) : ApiResult<Nothing>()
    data object Exception : ApiResult<Nothing>()
}
