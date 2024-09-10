package com.example.projekat.cats.quiz.leaderboard

import com.example.projekat.cats.api.model.ResultModel

interface LeaderboardState {
    data class LeaderboardState(
        val loading: Boolean = false,
        val error: DetailsError? = null,
        val results: List<ResultModel> = emptyList(),
        val nick: String = ""
    ) {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }
}