package com.example.projekat.cats.quiz.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat.cats.repository.BreedsRepository
import com.example.projekat.navigation.result
import com.example.projekat.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedsRepository,
    private val usersDataStore: UsersDataStore
) : ViewModel() {
    private val result: Float = savedStateHandle.result
    private val _resultState = MutableStateFlow(ResultState.ResultState())
    val resultState = _resultState.asStateFlow()
    private val _resultEvents = MutableSharedFlow<ResultState.ResultUIEvent>()
    fun setEvent(event: ResultState.ResultUIEvent) =
        viewModelScope.launch { _resultEvents.emit(event) }

    private fun setResultSate(update: ResultState.ResultState.() -> ResultState.ResultState) =
        _resultState.getAndUpdate(update)

    init {
        observeResult()
        observeEvents()
    }

    private fun observeEvents(){
        viewModelScope.launch {
            _resultEvents.collect {resultUIEvent ->
                when(resultUIEvent){
                    is ResultState.ResultUIEvent.PostResult -> post()
                }
            }
        }
    }

    private fun observeResult(){
        viewModelScope.launch {
            setResultSate { copy(isLoading = true) }
            try {
                setResultSate { copy(
                    category = category,
                    username = usersDataStore.data.value.users[usersDataStore.data.value.pick].nickname,
                    points = result
                ) }
            }catch (error: IOException){
                setResultSate { copy(error = ResultState.ResultState.DetailsError.DataUpdateFailed(cause = error)) }
            }finally {
                setResultSate { copy( isLoading = false) }
            }
        }
    }


    private fun post(){
        viewModelScope.launch {
            setResultSate { copy(isLoading = true) }
            withContext(Dispatchers.IO) {
                val state = resultState.value
                repository.postResult(state.username,state.points,1)
            }
            setResultSate { copy(isPosted = true, isLoading = false) }
        }
    }

}