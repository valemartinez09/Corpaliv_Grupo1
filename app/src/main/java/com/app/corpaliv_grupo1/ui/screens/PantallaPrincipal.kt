package com.app.corpaliv_grupo1.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.corpaliv_grupo1.viewmodel.EstadoViewModel

@Composable
fun PantallaPrincipal(
    modifier: Modifier = Modifier,
    viewModel: EstadoViewModel = viewModel()
) {
    val estado by viewModel.activo.collectAsState()
    val mostrarMensaje by viewModel.mostrarMensaje.collectAsState()

    if (estado == null) {
        // Mientras carga, solo se ve el loader
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val estaActivo = estado == true

        // El color cambia con una animación de 500 ms según el estado
        val colorAnimado by animateColorAsState(
            targetValue = if (estaActivo) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline,
            animationSpec = tween(durationMillis = 500),
            label = "colorModo"
        )

        // Texto derivado del estado
        val textoBoton by remember(estaActivo) {
            derivedStateOf { if (estaActivo) "Desactivar modo accesible" else "Activar modo accesible" }
        }

        // rememberSaveable: sobrevive a rotaciones de pantalla
        var cambios by rememberSaveable { mutableIntStateOf(0) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    cambios++
                    viewModel.alternarEstado()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorAnimado),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(textoBoton, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Cambios realizados: $cambios")

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje que aparece y desaparece con animación
            AnimatedVisibility(
                visible = mostrarMensaje,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = "¡Estado guardado exitosamente!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}