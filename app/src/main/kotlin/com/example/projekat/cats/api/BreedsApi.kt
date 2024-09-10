package com.example.projekat.cats.api

import com.example.projekat.cats.db.BreedsData
import com.example.projekat.cats.db.images.BreedsGallery
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreedsApi {

    @GET("breeds")
    suspend fun getAllBreeds() : List<BreedsData>

    @GET("breeds/{id}")
    suspend fun getBreed(@Path("id") id: String): BreedsData

    @GET("images/search?limit=20")
    suspend fun getAllBreedsPhotos(@Query("breed_ids") id: String): List<BreedsGallery>

    @GET("images/search?limit=1&order=ASC")
    suspend fun getBreedProfilePhoto(@Query("breed_ids") id: String): List<BreedsGallery>
}