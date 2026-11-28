package com.app.corpaliv_grupo1.viewmodel

import com.app.corpaliv_grupo1.navigation.NavigationEvent
import com.app.corpaliv_grupo1.navigation.Screen
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : FunSpec({

    beforeTest { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterTest { Dispatchers.resetMain() }

    // Crea el VM, se suscribe a sus eventos, ejecuta la acción y devuelve lo recibido
    fun eventosTras(accion: (MainViewModel) -> Unit): List<NavigationEvent> {
        val vm = MainViewModel()
        val recibidos = mutableListOf<NavigationEvent>()
        val scope = CoroutineScope(UnconfinedTestDispatcher())
        val job = scope.launch { vm.navigationEvents.collect { recibidos.add(it) } }

        accion(vm)

        job.cancel()
        return recibidos
    }

    test("navigateTo emite un evento NavigateTo con la pantalla") {
        eventosTras { it.navigateTo(Screen.Profile) } shouldBe
                listOf(NavigationEvent.NavigateTo(route = Screen.Profile))
    }

    test("navigateBack emite PopBackStack") {
        eventosTras { it.navigateBack() } shouldBe listOf(NavigationEvent.PopBackStack)
    }

    test("navigateUp emite NavigateUp") {
        eventosTras { it.navigateUp() } shouldBe listOf(NavigationEvent.NavigateUp)
    }

    test("varios eventos llegan en orden") {
        eventosTras {
            it.navigateTo(Screen.Home)
            it.navigateBack()
        } shouldBe listOf(
            NavigationEvent.NavigateTo(route = Screen.Home),
            NavigationEvent.PopBackStack
        )
    }
})