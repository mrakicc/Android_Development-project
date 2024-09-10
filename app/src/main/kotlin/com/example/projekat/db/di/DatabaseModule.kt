package com.example.projekat.db.di

import com.example.projekat.cats.db.BreedsDao
import com.example.projekat.cats.db.images.BreedsGalleryDao
import com.example.projekat.db.AppDatabase
import com.example.projekat.db.AppDatabaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(builder: AppDatabaseBuilder): AppDatabase {
        return builder.build()
    }

    @Provides
    fun provideBreedsDao(database: AppDatabase): BreedsDao {
        return database.breedsDao()
    }
    @Provides
    fun provideBreedsGalleryDao(database: AppDatabase): BreedsGalleryDao = database.breedsGalleryDao()
}