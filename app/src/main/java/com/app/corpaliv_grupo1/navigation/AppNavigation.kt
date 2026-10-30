package com.app.corpaliv_grupo1.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.corpaliv_grupo1.ui.screens.AportePersonaScreen
import com.app.corpaliv_grupo1.ui.screens.ColaborarScreen
import com.app.corpaliv_grupo1.ui.screens.FormularioScreen
import com.app.corpaliv_grupo1.ui.screens.InicioScreen
import com.app.corpaliv_grupo1.ui.screens.LoginFuncionarioScreen
import com.app.corpaliv_grupo1.ui.screens.PanelFuncionarioScreen
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

        composable(Routes.LoginFuncionario.route) { LoginFuncionarioScreen(navController) }
        composable(Routes.PanelFuncionario.route) { PanelFuncionarioScreen(navController) }
    }
}