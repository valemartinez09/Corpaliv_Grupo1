package com.app.corpaliv_grupo1.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.app.corpaliv_grupo1.ui.utils.obtenerWindowSizeClass


@Composable
fun HomeScreenAdaptativa() {
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
        WindowWidthSizeClass.Medium -> HomeScreenMediana()
        else -> HomeScreenExpandida()
    }
}