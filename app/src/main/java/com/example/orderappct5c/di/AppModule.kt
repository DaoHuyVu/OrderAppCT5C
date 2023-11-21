package com.example.orderappct5c.di

import com.example.orderappct5c.api.login.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.4:9192")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService = retrofit.create(LoginService::class.java)
}