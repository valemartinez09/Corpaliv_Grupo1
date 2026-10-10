package com.app.corpaliv_grupo1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.corpaliv_grupo1.R

// Vista EXPANDIDA (tablets grandes / escritorio): imagen grande + dos columnas de contenido
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("CORPALIV - Alba Lab") }) }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(48.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo CORPALIV",
                modifier = Modifier.weight(1f).height(300.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "¡Bienvenido a Alba Lab!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Promovemos la autonomía y la inclusión social y laboral de personas con discapacidad múltiple.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { /* acción futura */ }) { Text("Quiero colaborar") }
                    OutlinedButton(onClick = { /* acción futura */ }) { Text("Ver programas") }
                }
            }
        }
    }
}

@Preview(name = "Expanded", widthDp = 1000, heightDp = 800)
@Composable
fun PreviewExpandida() {
    HomeScreenExpandida()
}