package com.example.projekat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projekat.cats.db.BreedsDao
import com.example.projekat.cats.db.BreedsData
import com.example.projekat.cats.db.images.BreedsGallery
import com.example.projekat.cats.db.images.BreedsGalleryDao


@Database(
    entities = [
        BreedsData::class,
        BreedsGallery::class,
    ],
    version = 2,
    exportSchema = true,

    )

abstract class AppDatabase : RoomDatabase() {
    abstract fun breedsDao(): BreedsDao
    abstract fun breedsGalleryDao(): BreedsGalleryDao
}