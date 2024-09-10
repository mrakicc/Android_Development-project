package com.example.projekat.cats.gallery.photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat.cats.repository.BreedsRepository
import com.example.projekat.navigation.breedsId
import com.example.projekat.navigation.photoIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
data class BreedsPhotoViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val repository: BreedsRepository
): ViewModel(){
    private val breedsId: String = savedStateHandle.breedsId
    private val photoIndex: Int  = savedStateHandle.photoIndex
    private val _photoState = MutableStateFlow(BreedsPhotoState.BreedsPhotoState(photoIndex = photoIndex))
    val photoState = _photoState.asStateFlow()

    private fun setPhotoState(update: BreedsPhotoState.BreedsPhotoState.()->BreedsPhotoState.BreedsPhotoState) =
        _photoState.getAndUpdate(update)

    init {
        observeBreedsPhoto()
    }

    private fun observeBreedsPhoto(){
        viewModelScope.launch {
            setPhotoState { copy(loading = true) }
            try{
                repository.getAllBreedsPhotoByIdFLow(id = breedsId).collect{
                        photos->
                    setPhotoState { copy(photos=photos, loading = false) }
                }
            }catch (error: IOException){
                setPhotoState { copy(error = BreedsPhotoState.BreedsPhotoState.DetailsError.DataUpdateFailed(cause = error)) }
            }finally {
                setPhotoState { copy(photos = photos, loading = false) }
            }
        }
    }
}