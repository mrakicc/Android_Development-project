package com.example.projekat.users

import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar

@Serializable
data class User(
    val name: String,
    val email: String,
    val nickname: String,
    val darkTheme: Boolean = false,
    val guessCat: UserQuiz = UserQuiz.EMPTY
) {
    companion object {
        val EMPTY = User(
            name = "",
            email = "",
            nickname = "",
            darkTheme = false,
            guessCat = UserQuiz.EMPTY
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return email == other.email
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }

 }

    @Serializable
    data class UserQuiz(
        val resultsHistory: List<Result> = emptyList(),
        val bestResult: Float = 0f,
    ) {
        companion object {
            val EMPTY = UserQuiz(
                resultsHistory = emptyList(),
                bestResult = 0f,
            )
        }
    }

    @Serializable
    data class Result(
        val result: Float = 0f,
        val createdAt: Long
    ) {
        fun covertToDate(): String {
            return getDate(createdAt)
        }

        private fun getDate(milliSeconds: Long): String {
            val formatter = SimpleDateFormat("dd/MM/yyy hh:mm:ss")
            val calendar: Calendar = Calendar.getInstance()
            calendar.setTimeInMillis(milliSeconds)
            return formatter.format(calendar.time)
        }

    }
