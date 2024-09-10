package com.example.projekat.cats.quiz.guessFact

import com.example.projekat.cats.db.BreedsData
import com.example.projekat.cats.quiz.Timer
import com.example.projekat.users.Result
import com.example.projekat.users.UsersData

interface GuessFactState {
    data class GuessFactState(
        val isLoading: Boolean = false,
        val usersData: UsersData,
        val result: Result? = null,
        val error: DetailsError? = null,
        val breeds: List<BreedsData> = emptyList(),
        val answers: List<String> = emptyList(),
        val rightAnswer: String = "",
        val image: String = "",
        val points: Int = 0,
        val questionIndex: Int = 0,
        val question: Int = 0,
        val answerUser: String = "",
        val timer: Int = 60* Timer.MINUTES //5min
    ) {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }
    sealed class GuessFactUIEvent {
        data class CalculatePoints(val answerUser: String) : GuessFactUIEvent()
    }
}