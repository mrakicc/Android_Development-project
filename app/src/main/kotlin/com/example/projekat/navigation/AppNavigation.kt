package com.example.projekat.navigation

import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import breeds
import com.example.projekat.cats.details.breedsDetailsScreen
import com.example.projekat.cats.gallery.breedsGalleryScreen
import com.example.projekat.cats.gallery.photo.breedsPhotoScreen
import com.example.projekat.cats.login.loginScreen
import com.example.projekat.cats.quiz.guessFact.guessFactScreen
import com.example.projekat.cats.quiz.leaderboard.leaderboardScreen
import com.example.projekat.cats.quiz.result.resultScreen
import com.example.projekat.users.edit.editScreen
import com.example.projekat.users.history.historyScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "cats/login",
        enterTransition = {
            slideInHorizontally(
                animationSpec = spring(),
                initialOffsetX = {it},
            )
        },
        exitTransition =  { scaleOut(targetScale = 0.75f) },
        popEnterTransition = { scaleIn(initialScale = 0.75f) },
        popExitTransition = { slideOutHorizontally { it } },
    ) {
        loginScreen(
            route = "cats/login",
            navController = navController,
        )

        breeds(
            route = "breeds",
            navController = navController,
            goToQuiz = {
                navController.navigate("quiz/guess-fact")
            },
        )

        breedsDetailsScreen(
            route ="breeds/{id}",
            navController = navController,
            arguments = listOf(navArgument("id"){
                type = NavType.StringType
            })
        )

        breedsGalleryScreen(
            route = "images/{id}",
            navController = navController,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            }),
            onPhotoClicked = {id,photoIndex->
                navController.navigate(route = "photo/${id}/${photoIndex}")
            }
        )

        breedsPhotoScreen(
            route ="photo/{id}/{photoIndex}",
            navController = navController,
            arguments = listOf(navArgument("id"){
                type = NavType.StringType
            }, navArgument("photoIndex"){
                type = NavType.IntType
            })
        )

        guessFactScreen(
            route = "quiz/guess-fact",
            navController = navController
        )


        resultScreen(
            route = "quiz/result/{category}/{result}",
            navController = navController,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.IntType
                }, navArgument("result") {
                    type = NavType.FloatType
                }  )
        )

        leaderboardScreen(
            route = "quiz/leaderboard/{category}",
            navController = navController,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.IntType
                }
            )
        )

        historyScreen(
            route = "history",
            navController = navController
        )

        editScreen(
            route = "user/edit",
            navController = navController
        )


    }


}

inline val SavedStateHandle.breedsId: String
    get() = checkNotNull(get("id")) {"breedsId is mandatory"}

inline val SavedStateHandle.photoIndex: Int
    get() = checkNotNull(get("photoIndex")) {"photoIndex is mandatory"}

inline val SavedStateHandle.category: Int
    get() = checkNotNull(get("category")) {"category is mandatory"}

inline val SavedStateHandle.result: Float
    get() = checkNotNull(get("result")) {"result is mandatory"}