package com.example.projekat.cats.details

import com.example.projekat.cats.db.BreedsData

interface BreedsDetailsState {

    data class BreedsDetailsState(
        val breedsId: String,
        val loading: Boolean = false,
        val data: BreedsData? = null,
        val error: DetailsError? = null,
    )
    {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }

}