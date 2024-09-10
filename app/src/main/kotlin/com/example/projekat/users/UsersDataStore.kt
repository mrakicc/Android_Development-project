package com.example.projekat.users

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import okhttp3.internal.toImmutableList
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

@Singleton
class UsersDataStore @Inject constructor(
    private val dataStore: DataStore<UsersData>
){
    private val scope = CoroutineScope(Dispatchers.IO)

    val data = dataStore.data.stateIn(
        scope =scope,
        started = SharingStarted.Eagerly,
        initialValue = runBlocking { dataStore.data.first() }
    )

    private suspend fun updateList(users: List<User>, pick: Int = data.value.pick): UsersData {
        dataStore.updateData {
            it.copy(users = emptyList(), pick = -1)
        }

        return dataStore.updateData {
            it.copy(users = users, pick = pick)
        }
    }

    suspend fun updateUser(users: List<User>): UsersData {
        return updateList(users = users)
    }

    suspend fun addUser(user: User): UsersData {
        val users = data.value.users.toMutableList()
        users.add(user)
        return updateList(users = users.toImmutableList(), pick = users.size - 1)
    }


    suspend fun addGuessCatResult(result: Result): UsersData {
        val users = data.value.users.toMutableList()
        var guessCat = users[data.value.pick].guessCat
        val resultsHistory = guessCat.resultsHistory.toMutableList()

        resultsHistory.add(result)
        guessCat = guessCat.copy(
            resultsHistory = resultsHistory.toImmutableList(),
            bestResult = max(guessCat.bestResult, result.result),
        )
        users[data.value.pick] = users[data.value.pick].copy(guessCat = guessCat)

        return updateList(users = users.toImmutableList())
    }

}