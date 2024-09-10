package com.example.projekat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.projekat.navigation.AppNavigation
import theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.projekat.users.UsersDataStore
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var usersDataStore: UsersDataStore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val usersData by usersDataStore.data.collectAsState()
            AppTheme(darkTheme = if(usersData.pick == -1) false else usersData.users[usersData.pick].darkTheme) {
                AppNavigation()
            }

        }
    }
}
