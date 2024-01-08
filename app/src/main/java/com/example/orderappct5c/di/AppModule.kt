package com.example.orderappct5c.di


import com.example.orderappct5c.api.auth.AuthService
import com.example.orderappct5c.api.cart.CartService
import com.example.orderappct5c.api.menu.TokenInterceptor
import com.example.orderappct5c.api.menu.okhttpannotation.TokenInterceptorOkHttp
import com.example.orderappct5c.api.menu.MenuService
import com.example.orderappct5c.api.menu.dispatchers.IODispatchers
import com.example.orderappct5c.api.order.OrderDetailsService

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit.Builder
import retrofit2.converter.moshi.MoshiConverterFactory

import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit() : Builder {
        return Builder()
            .baseUrl("https://terrier-modern-violently.ngrok-free.app")
            .addConverterFactory(MoshiConverterFactory.create())
    }
    @Provides
    @Singleton
    @TokenInterceptorOkHttp
    fun provideMenuInterceptorOkHttp(
        tokenInterceptor: TokenInterceptor
    ) : OkHttpClient = OkHttpClient.Builder().addInterceptor(tokenInterceptor).build()

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Builder): AuthService
        = retrofit.build().create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideMenuService(
        retrofit: Builder,
        @TokenInterceptorOkHttp okHttpClient: OkHttpClient
    ) : MenuService
        = retrofit.client(okHttpClient).build().create(MenuService::class.java)
    @Provides
    @Singleton
    fun provideCartService(
        retrofit: Builder,
        @TokenInterceptorOkHttp okHttpClient: OkHttpClient
    ) : CartService = retrofit.client(okHttpClient).build().create(CartService::class.java)

    @Provides
    @Singleton
    @IODispatchers
    fun provideDispatchers() : CoroutineDispatcher = Dispatchers.IO
    @Provides
    @Singleton
    fun provideOrderService(
        retrofit: Builder,
        @TokenInterceptorOkHttp okHttpClient: OkHttpClient
    ) : OrderDetailsService = retrofit.client(okHttpClient).build().create(OrderDetailsService::class.java)
}