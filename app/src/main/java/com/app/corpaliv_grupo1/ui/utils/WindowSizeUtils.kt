package com.app.corpaliv_grupo1.ui.utils

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    // En el Preview no hay Activity: usamos un tamaño de celular por defecto
    if (LocalInspectionMode.current) {
        return WindowSizeClass.calculateFromSize(DpSize(360.dp, 800.dp))
    }
    return calculateWindowSizeClass(LocalContext.current as Activity)
}