package com.example.networking

import com.example.projekat.networking.di.BaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProdNetworkingModule {

    @BaseUrl
    @Provides
    fun provideProdUrl(): String = "https://api.thecatapi.com/v1/"
}