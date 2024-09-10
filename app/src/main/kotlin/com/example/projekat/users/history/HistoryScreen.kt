package com.example.projekat.users.history

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.projekat.core.TopBar
import com.example.projekat.users.Result

fun NavGraphBuilder.historyScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {

    val historyViewModel: HistoryViewModel = hiltViewModel()
    val historyState by historyViewModel.historyState.collectAsState()

    Surface(
        tonalElevation = 1.dp
    ) {
        Scaffold(
            topBar = { TopBar(onBackClick = {navController.navigateUp()}) }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(20.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly

                ) {
                    OutlinedCard(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                        border = BorderStroke(1.dp, Color.Magenta),
                    ){
                        CardContent(
                            bestResult = historyViewModel.getBestResult(),
                            historyState = historyState,
                            title = "Guess Cat",
                            results = historyViewModel.getAllResults(),
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun CardContent(
    bestResult: String,
    historyState: HistoryState.HistoryState,
    title: String,
    results: List<Result>,
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(3 / 4f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Best Result: $bestResult",
                    style = MaterialTheme.typography.labelLarge,

                    )
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Points",
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(
                            text = "Date",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(items = results) { result ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                HorizontalDivider()
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = result.result.toString(),
                                    )
                                    Text(
                                        text = result.covertToDate(),
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}