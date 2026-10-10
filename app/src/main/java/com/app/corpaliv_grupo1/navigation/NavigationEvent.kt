package com.app.corpaliv_grupo1.navigation

// Eventos de navegación que emite el ViewModel
sealed class NavigationEvent {

    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    data object PopBackStack : NavigationEvent()

    data object NavigateUp : NavigationEvent()
}