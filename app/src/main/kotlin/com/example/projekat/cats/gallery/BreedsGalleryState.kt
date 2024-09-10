package com.example.projekat.cats.gallery

interface BreedsGalleryState {
    data class BreedsGalleryState(
        val loading: Boolean = false,
        val photos: List<String> = emptyList(),
        val error: DetailsError? = null,
        val breedsId: String
    )
    {
        sealed class DetailsError{
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }
}