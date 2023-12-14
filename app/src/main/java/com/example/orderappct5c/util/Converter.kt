package com.example.orderappct5c.util

import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlin.reflect.KClass


class Converter {
    companion object{
        inline fun <reified T : Any> stringToObject(string : String) : T{
                return Gson().fromJson(string,T::class.java)
        }
    }
}