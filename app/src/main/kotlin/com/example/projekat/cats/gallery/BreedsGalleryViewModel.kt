package com.example.projekat.cats.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat.cats.repository.BreedsRepository
import com.example.projekat.navigation.breedsId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreedsGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedsRepository
): ViewModel(){
    private val breedsId: String = savedStateHandle.breedsId
    private val _galleryState = MutableStateFlow(BreedsGalleryState.BreedsGalleryState(breedsId = breedsId))
    val galleryState = _galleryState.asStateFlow()

    private fun setState (update: BreedsGalleryState.BreedsGalleryState.() -> BreedsGalleryState.BreedsGalleryState) =
        _galleryState.getAndUpdate(update)

    init {
        observeBreedsGallery()
    }

    private fun observeBreedsGallery() {

        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                var newPhotos = repository.getAllBreedsPhotoByIdFLow(id = breedsId).first()
                if(newPhotos.isEmpty()) {
                    withContext(Dispatchers.IO) {
                        repository.getAllBreedsPhotosApi(id = breedsId)
                    }
                    newPhotos = repository.getAllBreedsPhotoByIdFLow(id = breedsId).first()
                }
                setState { copy(photos = newPhotos, loading = false) }

            }catch (error: IOException){
                setState { copy(error = BreedsGalleryState.BreedsGalleryState.DetailsError.DataUpdateFailed(cause = error)) }
            }finally {
                setState { copy(photos = photos, loading = false) }
            }

        }
    }
}