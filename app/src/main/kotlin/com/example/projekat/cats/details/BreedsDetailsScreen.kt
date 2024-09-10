package com.example.projekat.cats.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.projekat.cats.db.BreedsData
import com.example.projekat.core.TopBar

fun NavGraphBuilder.breedsDetailsScreen(
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>
) = composable(route = route,arguments = arguments){
        navBackStackEntry->
    val breedsDetailsViewModel: BreedsDetailsViewModel = hiltViewModel(navBackStackEntry)
    val breedsState by breedsDetailsViewModel.stateDetails.collectAsState()

    Surface(
        tonalElevation = 1.dp
    ) {
        Scaffold (
            topBar = {
                TopBar(onBackClick = {navController.navigateUp()})
            },
            content = { paddingValues ->
                BreedsDeatilsScreen(
                    breedsState = breedsState,
                    paddingValues = paddingValues,
                    openGallery = {id ->  navController.navigate("images/${id}")}
                )

            }
        )
    }

}

@Composable
private fun BreedsDeatilsScreen(
    breedsState: BreedsDetailsState.BreedsDetailsState,
    paddingValues: PaddingValues,
    openGallery: (String)-> Unit,
) {
    if (breedsState.loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    else if (breedsState.error != null) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val errorMessage = when (breedsState.error) {
                is BreedsDetailsState.BreedsDetailsState.DetailsError.DataUpdateFailed ->
                    "Failed to load. Error message: ${breedsState.error.cause?.message}."
            }

            Text(text = errorMessage, fontSize = 20.sp)
        }
    }
    else if (breedsState.data == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "There is no data for id ${breedsState.breedsId}",
                fontSize = 20.sp
            )
        }
    }
    else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {

            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
//                SubcomposeAsyncImage(
//                    modifier =  Modifier.fillMaxWidth(),
//                    model = breedsState.data.image?.url ?: "",
//                    contentDescription = null,
//                    loading = {
//                        Box(modifier = Modifier.fillMaxSize()) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.align(Alignment.Center)
//                            )
//                        }
//                    }
//                )
                BreedsInfo(
                    breedsState = breedsState ,
                    data = breedsState.data,
                    openGallery = openGallery)

            }
        }
    }
}



@Composable
private fun BreedsInfo(
    breedsState: BreedsDetailsState.BreedsDetailsState,
    data: BreedsData,
    openGallery: (String)-> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Button(
            onClick = {
                openGallery(breedsState.breedsId)
            }
        ) {
            Text(text = "Gallery")
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Race Of Cat",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = data.name,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = data.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Countries Of Origin",
                style = MaterialTheme.typography.titleMedium
            )

            data.origin.replace(" ", "").split(",").forEach { country ->
                Text(
                    text = country,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "Temperaments",
                style = MaterialTheme.typography.titleMedium
            )
            data.temperament.replace(" ", "").split(",").forEach { temperament ->
                Text(
                    text = temperament,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Life Span",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = data.life_span,
                style = MaterialTheme.typography.bodyMedium
            )
        }


        RatingBar(text = "affectionLevel", rating = data.affection_level.toFloat())
        RatingBar(text = "dogFriendly", rating = data.dog_friendly.toFloat())

        val context = LocalContext.current

        Button(
            onClick =
            {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(data.wikipedia_url ?: "")
                    )
                )
            }
        ) {
            Text(text = "Wiki")
        }

    }
}

@Composable
private fun RatingBar(
    text: String,
    maxStars: Int = 5,
    rating: Float,
) {
    val density = LocalDensity.current.density

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {

        Text(text = "$text:")

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.selectableGroup(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = {rating / maxStars.toFloat()}
            )
        }
    }
}
