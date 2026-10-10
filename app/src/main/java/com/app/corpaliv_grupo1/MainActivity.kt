package com.app.corpaliv_grupo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.corpaliv_grupo1.navigation.NavigationEvent
import com.app.corpaliv_grupo1.navigation.Screen
import com.app.corpaliv_grupo1.ui.HomeScreen
import com.app.corpaliv_grupo1.ui.screens.HomeScreenAdaptativa
import com.app.corpaliv_grupo1.ui.screens.ProfileScreen
import com.app.corpaliv_grupo1.ui.screens.SettingsScreen
import com.app.corpaliv_grupo1.ui.theme.Corpaliv_Grupo1Theme
import com.app.corpaliv_grupo1.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Corpaliv_Grupo1Theme {

                // ViewModel y NavController
                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()

                // Escuchar eventos de navegación emitidos por el ViewModel
                LaunchedEffect(key1 = Unit) {
                    viewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route) {
                                    event.popUpToRoute?.let {
                                        popUpTo(route = it.route) {
                                            inclusive = event.inclusive
                                        }
                                    }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                            }

                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                            is NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }

                // Cada pantalla trae su propio Scaffold, no se anida otro aquí
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(navController, viewModel)
                    }
                    composable(Screen.Profile.route) {
                        ProfileScreen(navController, viewModel)
                    }
                    composable(Screen.Settings.route) {
                        SettingsScreen(navController, viewModel)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    Corpaliv_Grupo1Theme {
        HomeScreenAdaptativa()
    }
}