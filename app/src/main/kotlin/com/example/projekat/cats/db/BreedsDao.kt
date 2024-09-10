package com.example.projekat.cats.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedsData>)

    @Query("SELECT * FROM BreedsData")
    fun getAll(): Flow<List<BreedsData>>

    @Query("SELECT * FROM BreedsData WHERE id =:breedId")
    fun getBreedById(breedId: String): Flow<BreedsData>

    @Query("UPDATE BreedsData SET profilePhoto = :photoUrl WHERE id = :id")
    suspend fun updateProfilePhoto(id: String, photoUrl: String)

    @Query("SELECT profilePhoto FROM BreedsData WHERE id =:breedId")
    suspend fun getProfilePhoto(breedId: String): String

}