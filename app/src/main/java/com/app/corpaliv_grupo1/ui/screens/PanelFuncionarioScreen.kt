package com.app.corpaliv_grupo1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.navigation.Routes

/** Panel del funcionario (provisorio): luego listará los registros de interés guardados. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelFuncionarioScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.panel_titulo)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.panel_proximamente),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    // Cerrar sesión: vuelve a Inicio y limpia el historial
                    navController.navigate(Routes.Inicio.route) {
                        popUpTo(Routes.Inicio.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_cerrar_sesion),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}