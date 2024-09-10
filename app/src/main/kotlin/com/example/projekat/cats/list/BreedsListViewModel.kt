package com.example.projekat.cats.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat.cats.repository.BreedsRepository
import com.example.projekat.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@HiltViewModel
class BreedsListViewModel @Inject constructor(
    private val repository: BreedsRepository,
    private val usersDataStore: UsersDataStore,
): ViewModel(){
    private val _state = MutableStateFlow(
        BreedList.BreedsListState(
            usersData = usersDataStore.data.value,
            darkTheme = usersDataStore.data.value.users[usersDataStore.data.value.pick].darkTheme,
        )
    )
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedList.BreedsListState.()-> BreedList.BreedsListState) = _state.update(reducer)

    private val _event = MutableSharedFlow<BreedList.BreedsListUiEvent>()
    fun setBreedsEvent(event: BreedList.BreedsListUiEvent) = viewModelScope.launch { _event.emit(event) }

    init {
        observeRepoBreeds()
        fetchAllBreeds()
        observeEvents()
    }

    private fun observeEvents(){
        viewModelScope.launch {
            _event.collect{ breedsListUiEvent->
                when(breedsListUiEvent){
                    is BreedList.BreedsListUiEvent.SreachQuery -> searchQueryFilter(breedsListUiEvent.query)
                    is BreedList.BreedsListUiEvent.ChangeTheme -> changeTheme(breedsListUiEvent.bool)
                }
            }
        }
    }


    private fun fetchAllBreeds(){
        viewModelScope.launch {
            setState { copy(updating=true) }
            try {

                withContext(Dispatchers.IO){

                    repository.fetchAllBreeds()
                }
                updateProfilePhotos()
               }catch (error: Exception){
                Log.e("Greska", "Error: ${error.message}", error)
            }finally {
                setState { copy(updating=false) }
            }
        }
    }

    private suspend fun updateProfilePhotos() {
        state.value.breeds.forEach { breed ->
            if(repository.getProfilePhotoDb(breed.id).equals(""))
                repository.getBreedProfilePhotoApi(breed.id)
        }
    }


    private fun observeRepoBreeds(){
        viewModelScope.launch {
            setState { copy(loading = true) }
            repository.getAllBreedsFlowFromDb().collect{ newBreedsList ->
              //  updateProfilePhotos()
                setState { copy(breeds = newBreedsList, loading = false) }

                searchQueryFilter(_state.value.search)
            }
        }
    }


    private fun searchQueryFilter(query: String){
        viewModelScope.launch {
            setState {
                copy(
                    breedsFilter =
                    if(query.isBlank())
                        breeds
                    else
                        breeds.filter { breed -> breed.doesMatchSearchQuery(query) } ,

                    search = query
                )
            }
        }
    }
    private fun changeTheme(bool: Boolean) {
        val users = usersDataStore.data.value.users.toMutableList()
        val pick = usersDataStore.data.value.pick

        users[pick] = users[pick].copy(
            darkTheme = bool
        )

        viewModelScope.launch {
            usersDataStore.updateUser(users.toImmutableList())
            setState { copy(darkTheme = bool) }
        }
    }


}

