package com.app.corpaliv_grupo1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.corpaliv_grupo1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // Scaffold: estructura base de la pantalla (barra superior + contenido)
    Scaffold(
        topBar = {
            //Barra de arriba
            TopAppBar(title = { Text("App Kotlin Corpaliv")},
                // Colores tomados desde MaterialTheme
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ))
        }
    ) { innerPadding ->
        Column(
            // Column: apila los elementos verticalmente
            modifier = Modifier
                .padding(innerPadding) // respeta el espacio de la barra superior
                .fillMaxSize()
                .padding(16.dp), // margen interno uniforme
            verticalArrangement = Arrangement.spacedBy(20.dp), // espacio entre elementos
        ) {
            Text(
                text = "Bienvenido",
            )
            Button(onClick = { }) {
                Text("Presióname")
            }
            // Logo de la app
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // R.drawable.logo
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
            // Texto secundario con color del tema
            Text(
                text = "Prueba de pantalla app",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Row: coloca los botones uno al lado del otro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = { }, modifier = Modifier.weight(1f)) {
                    Text("Aceptar")
                }
                OutlinedButton(onClick = { }, modifier = Modifier.weight(1f)) {
                    Text("Cancelar")
                }
            }
        }
    }
}
// Preview para ver la pantalla en Android Studio sin ejecutar la app
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
