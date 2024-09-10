package com.example.projekat.cats.quiz.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.resultScreen(
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>,
) = composable(route = route, arguments = arguments) { navBackStackEntry ->

    val resultViewModel: ResultViewModel = hiltViewModel()
    val state by resultViewModel.resultState.collectAsState()

    BackHandler() {}

    Surface(
        tonalElevation = 1.dp
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "RESULTS!",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            },
            content = { paddingValues ->
                ResultScreen(
                    state = state,
                    paddingValues = paddingValues,
                    leaderboard =
                    { category->
                        navController.navigate("quiz/leaderboard/${category}")
                    },
                    home = { navController.navigate("breeds") },
                    eventPublisher = { uiEvent -> resultViewModel.setEvent(uiEvent) },
                )
            }
        )
    }
}

@Composable
fun ResultScreen(
    state: ResultState.ResultState,
    paddingValues: PaddingValues,
    leaderboard: (Int) -> Unit,
    home: () -> Unit,
    eventPublisher: (uiEvent: ResultState.ResultUIEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "${state.username}, your result is ${state.points}!",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Do you want to share it with other players?",
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.CenterHorizontally)
        )
        Button(
            onClick = {
                eventPublisher(ResultState.ResultUIEvent.PostResult)
            },
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .align(Alignment.CenterHorizontally)
                .width(250.dp),
            enabled = !state.isPosted
        ) {
            Text(text = "Share it")
        }
        Button(
            onClick = {
                home()
            },
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .align(Alignment.CenterHorizontally)
                .width(250.dp)
        ) {
            Text(text = "Go back to home screen")
        }
        Button(
            onClick = {
                leaderboard(1)//state.category)
            },
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .align(Alignment.CenterHorizontally)
                .width(250.dp),
            enabled = !state.isLoading
        ) {
            Text(text = "Go to leaderboard")
        }
    }

}