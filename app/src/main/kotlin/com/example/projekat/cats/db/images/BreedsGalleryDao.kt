package com.example.projekat.cats.db.images

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedsGalleryDao {

    @Query("SELECT url FROM images WHERE id= :id")
    fun getAllImagesForId(id: String): Flow<List<String>>

    @Query("SELECT url FROM images WHERE url = :url")
    fun getImageByUrl(url: String): Flow<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGalleryBreeds(breeds: List<BreedsGallery>)

}