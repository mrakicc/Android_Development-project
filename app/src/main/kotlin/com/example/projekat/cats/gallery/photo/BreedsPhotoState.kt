package com.example.projekat.cats.gallery.photo

interface BreedsPhotoState {
    data class BreedsPhotoState(
        val loading: Boolean = false,
        val photos: List<String> = emptyList(),
        val photoIndex: Int  = 0,
        val error: DetailsError? = null,
    ){
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }
}