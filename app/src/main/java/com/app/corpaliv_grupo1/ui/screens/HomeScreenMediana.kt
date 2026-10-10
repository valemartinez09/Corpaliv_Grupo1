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

// Vista MEDIANA (tablets chicas): imagen y texto lado a lado
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("CORPALIV - Alba Lab") }) }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo CORPALIV",
                modifier = Modifier.weight(1f).height(220.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "¡Bienvenido a Alba Lab!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Conoce nuestros programas de inclusión sociolaboral.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = { /* acción futura */ }) {
                    Text("Quiero colaborar")
                }
            }
        }
    }
}

@Preview(name = "Medium", widthDp = 700, heightDp = 800)
@Composable
fun PreviewMediana() {
    HomeScreenMediana()
}