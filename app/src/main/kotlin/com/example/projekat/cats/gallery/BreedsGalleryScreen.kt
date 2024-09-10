package com.example.projekat.cats.gallery

import com.example.projekat.core.AppIconButton

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.breedsGalleryScreen (
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>,
    onPhotoClicked: (breedsId: String, photoIndex: Int) -> Unit
) = composable(route = route, arguments = arguments) { navBackStackEntry ->

    val breedsGalleryViewModel: BreedsGalleryViewModel = hiltViewModel(navBackStackEntry)
    val state by breedsGalleryViewModel.galleryState.collectAsState()


    Surface(
        tonalElevation = 1.dp
    ) {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Photo gallery", fontWeight = FontWeight.Bold)
                    },
                    navigationIcon = {
                        AppIconButton(imageVector = Icons.AutoMirrored.Filled.ArrowBack, onClick = {navController.navigateUp()})
                    }
                )
            },
            content = { paddingValues ->
                BreedsGalleryScreen(
                    state = state,
                    paddingValues = paddingValues,
                    onPhotoClicked = onPhotoClicked
                )
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedsGalleryScreen(
    state: BreedsGalleryState.BreedsGalleryState,
    paddingValues: PaddingValues,
    onPhotoClicked: (catId: String,photoNumber: Int) -> Unit
){
    if (state.loading) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else if (state.error != null) {
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val errorMessage = when (state.error) {
                is BreedsGalleryState.BreedsGalleryState.DetailsError.DataUpdateFailed ->
                    "Failed to load. Error message: ${state.error.cause?.message}."
            }

            Text(text = errorMessage, fontSize = 20.sp)
        }
    } else if (state.photos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "There is no data for that cat",
                fontSize = 20.sp
            )
        }
    } else {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),

            ) {

            itemsIndexed(items = state.photos,
                key = { index: Int, item: String ->
                    item
                }){index: Int, item: String ->
                SubcomposeAsyncImage(
                    modifier =  Modifier.fillMaxWidth().aspectRatio(1f).clickable {
                        onPhotoClicked(state.breedsId,index)
                    },
                    model = item,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                )
            }
        }
    }
}