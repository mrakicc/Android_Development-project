package com.example.projekat.users.history

import com.example.projekat.users.UsersData

interface HistoryState {
    data class HistoryState(
        val usersData: UsersData,
    )
}