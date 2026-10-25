package com.app.corpaliv_grupo1.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.corpaliv_grupo1.ui.HomeScreen
import com.app.corpaliv_grupo1.ui.screens.ProfileScreen
import com.app.corpaliv_grupo1.ui.screens.RegistroScreen
import com.app.corpaliv_grupo1.ui.screens.ResumenScreen
import com.app.corpaliv_grupo1.ui.screens.SettingsScreen
import com.app.corpaliv_grupo1.viewmodel.MainViewModel
import com.app.corpaliv_grupo1.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ViewModel de navegación (Guía 10)
    val viewModel: MainViewModel = viewModel()

    // ViewModel del formulario: se crea UNA sola vez y se comparte entre registro y resumen
    val usuarioViewModel: UsuarioViewModel = viewModel()

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

    NavHost(
        navController = navController,
        startDestination = "registro", // para volver a la demo anterior: Screen.Home.route
        modifier = Modifier.fillMaxSize()
    ) {
        // Rutas de la Guía 10
        composable(Screen.Home.route) {
            HomeScreen(navController, viewModel)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController, viewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController, viewModel)
        }

        // Rutas de la Guía 11 (el Scaffold respeta la barra de estado)
        composable("registro") {
            Scaffold { innerPadding ->
                Box(Modifier.padding(innerPadding)) {
                    RegistroScreen(navController, usuarioViewModel)
                }
            }
        }
        composable("resumen") {
            Scaffold { innerPadding ->
                Box(Modifier.padding(innerPadding)) {
                    ResumenScreen(usuarioViewModel)
                }
            }
        }
    }
}