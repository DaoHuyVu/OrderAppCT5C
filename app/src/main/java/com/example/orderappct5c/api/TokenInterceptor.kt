package com.example.orderappct5c.api

import com.example.orderappct5c.util.DataStoreUtil
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
    private val dataStoreUtil : DataStoreUtil
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = dataStoreUtil.getToken()
        val newRequest = request.newBuilder().addHeader(AUTHORIZATION,"Bearer $token").build()
        return chain.proceed(newRequest)
    }
    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}