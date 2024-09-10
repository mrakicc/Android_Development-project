package com.example.projekat.cats.quiz.leaderboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat.cats.repository.BreedsRepository
import com.example.projekat.navigation.category
import com.example.projekat.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedsRepository,
    private val usersDataStore: UsersDataStore
) : ViewModel() {

    private val category: Int = savedStateHandle.category
    private val _leaderboardState = MutableStateFlow(LeaderboardState.LeaderboardState())
    val leaderboardState = _leaderboardState.asStateFlow()

    private fun setLeaderboardState (update: LeaderboardState.LeaderboardState.() -> LeaderboardState.LeaderboardState) =
        _leaderboardState.getAndUpdate(update)

    init {
        observeResults()
    }

    private fun observeResults() {

        viewModelScope.launch {
            setLeaderboardState { copy(loading = true) }
            try {
                val list = repository.fetchAllResultsForCategory(category = category)
                setLeaderboardState { copy(results = list, nick = usersDataStore.data.value.users[usersDataStore.data.value.pick].nickname) }
            }catch (error: IOException){
                setLeaderboardState { copy(error = LeaderboardState.LeaderboardState.DetailsError.DataUpdateFailed(cause = error)) }
            }finally {
                setLeaderboardState { copy(loading = false) }
            }

        }
    }

}