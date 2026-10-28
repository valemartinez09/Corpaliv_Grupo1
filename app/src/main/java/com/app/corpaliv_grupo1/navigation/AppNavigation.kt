package com.app.corpaliv_grupo1.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.corpaliv_grupo1.ui.screens.AportePersonaScreen
import com.app.corpaliv_grupo1.ui.screens.ColaborarScreen
import com.app.corpaliv_grupo1.ui.screens.FormularioScreen
import com.app.corpaliv_grupo1.ui.screens.InicioScreen
import com.app.corpaliv_grupo1.ui.screens.QuienesSomosScreen
import com.app.corpaliv_grupo1.ui.screens.ProgramasScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val duracion = 350

    NavHost(
        navController = navController,
        startDestination = Routes.Inicio.route,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(duracion)) +
                    fadeIn(tween(duracion))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(duracion)) +
                    fadeOut(tween(duracion))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(duracion)) +
                    fadeIn(tween(duracion))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(duracion)) +
                    fadeOut(tween(duracion))
        }
    ) {
        composable(Routes.Inicio.route) { InicioScreen(navController) }
        composable(Routes.QuienesSomos.route) { QuienesSomosScreen(navController) }
        composable(Routes.Colaborar.route) { ColaborarScreen(navController) }
        composable(Routes.Programas.route) { ProgramasScreen(navController) }
        composable(Routes.AportePersona.route) { AportePersonaScreen(navController) }
        composable(
            route = Routes.Formulario.route,
            arguments = listOf(
                navArgument(Routes.Formulario.ARG_FRECUENCIA) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { FormularioScreen(navController) }

        // Relleno temporal hasta la Parte 4
        composable(Routes.LoginFuncionario.route) { Pendiente("Login funcionario (Parte 4)") }
        composable(Routes.PanelFuncionario.route) { Pendiente("Panel funcionario (Parte 4)") }

    }
}

@Composable
private fun Pendiente(texto: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(texto)
    }
}