package com.example.projekat.cats.gallery.photo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.example.projekat.core.AppIconButton

fun NavGraphBuilder.breedsPhotoScreen(
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>
) = composable(route = route, arguments = arguments) { navBackStackEntry ->

    val breedsPhotoViewModel: BreedsPhotoViewModel = hiltViewModel(navBackStackEntry)
    val breedsState by breedsPhotoViewModel.photoState.collectAsState()


    Surface(
        tonalElevation = 1.dp
    ) {
        BreedsPhotoScreen(
            breedsState = breedsState,
            onClose = { navController.navigateUp() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BreedsPhotoScreen(
    breedsState: BreedsPhotoState.BreedsPhotoState,
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Photo", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        onClick = onClose
                    )
                }
            )
        },
        content = {
            val pagerState = rememberPagerState(
                pageCount = {
                    breedsState.photos.size
                },
                initialPage = breedsState.photoIndex
            )


            if (breedsState.loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else if (breedsState.error != null) {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val errorMessage = when (breedsState.error) {
                        is BreedsPhotoState.BreedsPhotoState.DetailsError.DataUpdateFailed ->
                            "Failed to load. Error message: ${breedsState.error.cause?.message}."
                    }

                    Text(text = errorMessage, fontSize = 20.sp)
                }
            } else if (breedsState.photos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "There is no data for that cat",
                        fontSize = 20.sp
                    )
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    contentPadding = it
                ) { pageIndex ->
                    val photo = breedsState.photos[pageIndex]

                    SubcomposeAsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = photo,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }
        }
    )
}
