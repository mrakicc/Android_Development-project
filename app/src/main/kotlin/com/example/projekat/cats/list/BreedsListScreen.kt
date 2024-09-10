import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.projekat.cats.db.BreedsData
import com.example.projekat.cats.list.BreedList
import com.example.projekat.cats.list.BreedsListViewModel
import com.example.projekat.core.AppIconButton
import com.example.projekat.users.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.breeds(
    route: String,
    navController: NavController,
    goToQuiz: () -> Unit,
) = composable(
    route = route
){
    val breedsListViewModel: BreedsListViewModel = hiltViewModel()
    val state by breedsListViewModel.state.collectAsState()

    val scope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    if(state.usersData.pick == -1){
        navController.navigate("login")
    }else{
        Surface (
            tonalElevation = 1.dp
        ){
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    UserInfo(
                        state = state,
                        navigateToHistory = { navController.navigate("history") },
                        navigateToEdit = { navController.navigate("user/edit") },
                        leaderboard = { category ->
                            navController.navigate("quiz/leaderboard/${category}")
                        }
                    )
                }
            ) {
                Scaffold(
                    topBar={
                        TopAppBar(
                            title={
                                Text(text = "Breeds")
                            },
                            navigationIcon = {
                                AppIconButton(
                                    imageVector = Icons.Default.Menu,
                                    onClick = { scope.launch { drawerState.open() } }
                                )
                            },
                            actions = {
                                AppIconButton(
                                    imageVector = if (state.darkTheme) Icons.Outlined.LightMode else Icons.Filled.LightMode,
                                    onClick = {
                                        breedsListViewModel.setBreedsEvent(BreedList.BreedsListUiEvent.ChangeTheme(!state.darkTheme))
                                    })
                            }
                        )
                    },
                    floatingActionButton = {
                        LargeFloatingActionButton(
                            onClick = { goToQuiz() },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(text = "QUIZ", fontWeight = FontWeight.Bold)
                        }
                    },
                    content = {
                        BreedsList(
                            state = state,
                            paddingValues = it,
                            eventPublisher = { uiEvent -> breedsListViewModel.setBreedsEvent(uiEvent) },
                            onClick = { breedsInfoDetail -> navController.navigate("breeds/${breedsInfoDetail.id}") }
                        )
                    }
                )
            }
        }
    }
}
@Composable
private fun UserInfo(
    state: BreedList.BreedsListState,
    navigateToHistory: () -> Unit,
    navigateToEdit: () -> Unit,
    leaderboard: (Int) -> Unit
){
    BoxWithConstraints {
        val box = this
        ModalDrawerSheet(modifier = Modifier.width(box.maxWidth*3/4)) {
            Column(modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column {
                    Text(
                        text = "Account",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )


                    LazyColumn(
                        modifier = Modifier.heightIn(max = 180.dp)
                    ) {
                        itemsIndexed(state.usersData.users) { index, user ->
                            UserItemDrawer(
                                user = user,
                                index = index,
                                state = state,
                                navigateToEdit = navigateToEdit
                            )
                        }
                    }
                }
                HorizontalDivider()
                Column {
                    Text(
                        text = "History",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "See quiz's history",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        selected = false,
                        onClick = navigateToHistory
                    )
                }
                HorizontalDivider()

                Column {
                    Text(
                        text = "Leaderboards",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    NavigationDrawerItem(
                        icon = {
                            AppIconButton(
                                imageVector = Icons.Filled.Leaderboard,
                                onClick = {
                                    leaderboard(1)
                                }
                            )
                        },
                        label = {
                            Text(
                                text = "Guess Cat",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        selected = false,
                        onClick = {
                            leaderboard(1)
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun BreedsList(
    state: BreedList.BreedsListState,
    paddingValues: PaddingValues,
    eventPublisher: (uiEvent: BreedList.BreedsListUiEvent) -> Unit,
    onClick: (BreedsData) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        TextField(
            value = state.search,
            onValueChange = { text ->
                eventPublisher(BreedList.BreedsListUiEvent.SreachQuery(query = text))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = { Text(text = "Search") },
            shape = CircleShape,
            leadingIcon = { AppIconButton(imageVector = Icons.Default.Search, onClick = { }) }
        )

        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else if (state.error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val errorMessage = when (state.error) {
                    is BreedList.BreedsListState.Error.DataUpdateFailed ->
                        "Failed to load. Error message: ${state.error.cause?.message}."
                }

                Text(text = errorMessage, fontSize = 20.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = state.breedsFilter,
                    key = { breed -> breed.id }
                ) { breedDetail ->
                    BreedsDetails(
                        breed = breedDetail,
                        onClick = { onClick(breedDetail) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
@Composable
fun BreedsDetails(
    breed: BreedsData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Ovaj Row će sadržati profilnu sliku i ime mačke
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // Vertikalno centriranje
            ) {
                // Prvo ide profilna slika mačke
//                breed.profilePhoto?.let { photoUrl ->
//                    Box(
//                        modifier = Modifier
//                            .size(100.dp)
//                            .clip(MaterialTheme.shapes.medium)
//                            .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
//                    ) {
//                        SubcomposeAsyncImage(
//                            model = photoUrl,
//                            contentDescription = null,
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier.fillMaxSize()
//                        )
//                    }
//                }

                // Razmak između slike i imena
//                Spacer(modifier = Modifier.width(8.dp))

                // Ime mačke
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = breed.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!breed.alt_names.isNullOrEmpty()) {
                Text(text = "Alternative Names", style = MaterialTheme.typography.titleMedium)
                Column {
                    breed.alt_names.replace(" ", "").split(",").forEach { altName ->
                        Text(text = altName, style = MaterialTheme.typography.titleSmall)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(text = breed.description, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                breed.temperament.replace(" ", "").split(",").take(3).forEach {
                    AssistChip(
                        onClick = { },
                        label = { Text(text = it) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}

@Composable
private fun UserItemDrawer(
    user: User,
    index: Int,
    state: BreedList.BreedsListState,
    navigateToEdit: () -> Unit,
){
    NavigationDrawerItem(
        label = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Column {
                    Text(
                        text = user.nickname,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                if (index == state.usersData.pick) {
                    AppIconButton(
                        imageVector = Icons.Filled.Edit,
                        onClick = {
                            if (index == state.usersData.pick) {
                                navigateToEdit()
                            }
                        }
                    )
                }
            }
        },
        selected = index == (state.usersData.pick),
        onClick = {
            if (index == state.usersData.pick) {
                navigateToEdit()
            }
        }
    )
}


