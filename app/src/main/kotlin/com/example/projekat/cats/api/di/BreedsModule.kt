package com.example.projekat.cats.api.di

import com.example.projekat.cats.api.BreedsApi
import com.example.projekat.cats.api.ResultsApi
import com.example.projekat.networking.serialization.AppJson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
object BreedsModule {
    @Singleton
    @Provides
    @Named("BreedsApiRetrofit")
    fun provideBreedsApiRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideBreedsApi(@Named("BreedsApiRetrofit")retrofit: Retrofit): BreedsApi = retrofit.create(BreedsApi::class.java)

    @Singleton
    @Provides
    @Named("ResultRetrofit")
    fun provideResultRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(" https://rma.finlab.rs/")
        .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
        .build()

    @Singleton
    @Provides
    fun provideResultApi(@Named("ResultRetrofit") retrofit: Retrofit): ResultsApi = retrofit.create(ResultsApi::class.java)
}