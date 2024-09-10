package com.example.projekat.users.history

import androidx.lifecycle.ViewModel
import com.example.projekat.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val usersDataStore: UsersDataStore
) : ViewModel() {

    private val _historyState = MutableStateFlow(HistoryState.HistoryState(usersData = usersDataStore.data.value))
    val historyState = _historyState.asStateFlow()



    fun getBestResult(): String {
        val users = historyState.value.usersData.users
        val pick = historyState.value.usersData.pick

        return users[pick].guessCat.bestResult.toString()

    }

    fun getAllResults(): List<com.example.projekat.users.Result> {
        val users = historyState.value.usersData.users
        val pick = historyState.value.usersData.pick


         return users[pick].guessCat.resultsHistory.reversed()

        }
    }
