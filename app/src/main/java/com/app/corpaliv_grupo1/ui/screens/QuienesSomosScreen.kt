package com.app.corpaliv_grupo1.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuienesSomosScreen(navController: NavController) {
    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.quienes_titulo)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_volver)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 640.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.quienes_intro),
                    style = MaterialTheme.typography.titleMedium
                )

                TarjetaInfo(
                    icono = Icons.Default.Build,
                    titulo = R.string.quienes_trabajo_titulo,
                    texto = R.string.quienes_trabajo_texto,
                    contenedor = colores.primaryContainer,
                    sobreContenedor = colores.onPrimaryContainer,
                    acento = colores.primary,
                    sobreAcento = colores.onPrimary
                )
                TarjetaInfo(
                    icono = Icons.Default.Share,
                    titulo = R.string.quienes_vinculacion_titulo,
                    texto = R.string.quienes_vinculacion_texto,
                    contenedor = colores.secondaryContainer,
                    sobreContenedor = colores.onSecondaryContainer,
                    acento = colores.secondary,
                    sobreAcento = colores.onSecondary
                )
                TarjetaInfo(
                    icono = Icons.Default.Favorite,
                    titulo = R.string.quienes_ayuda_titulo,
                    texto = R.string.quienes_ayuda_texto,
                    contenedor = colores.tertiaryContainer,
                    sobreContenedor = colores.onTertiaryContainer,
                    acento = colores.tertiary,
                    sobreAcento = colores.onTertiary
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate(Routes.Colaborar.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_quiero_colaborar),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaInfo(
    icono: ImageVector,
    @StringRes titulo: Int,
    @StringRes texto: Int,
    contenedor: Color,
    sobreContenedor: Color,
    acento: Color,
    sobreAcento: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = contenedor,
            contentColor = sobreContenedor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = acento,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = sobreAcento
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(titulo),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(texto),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}