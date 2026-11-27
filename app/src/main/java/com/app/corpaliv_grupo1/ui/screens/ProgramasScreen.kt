package com.app.corpaliv_grupo1.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.model.CategoriaPrograma
import com.app.corpaliv_grupo1.navigation.Routes
import com.app.corpaliv_grupo1.viewmodel.ProgramasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramasScreen(
    navController: NavController,
    viewModel: ProgramasViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    // Las tarjetas aparecen una tras otra al abrir la pantalla
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.programas_titulo)) },
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
                    text = stringResource(R.string.programas_intro),
                    style = MaterialTheme.typography.titleMedium
                )

                val error = estado.error
                when {
                    estado.cargando -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    error != null -> {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(onClick = viewModel::cargar) {
                            Text(stringResource(R.string.btn_reintentar))
                        }
                    }

                    else -> {
                        estado.programas.forEachIndexed { indice, programa ->
                            val estilo = estiloDe(programa.categoria)
                            TarjetaAnimada(visible = visible, retardo = indice * 150) {
                                TarjetaPrograma(
                                    icono = estilo.icono,
                                    titulo = programa.titulo,
                                    texto = programa.descripcion,
                                    contenedor = estilo.contenedor,
                                    sobreContenedor = estilo.sobreContenedor,
                                    acento = estilo.acento,
                                    sobreAcento = estilo.sobreAcento
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate(Routes.Colaborar.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                    Spacer(Modifier.size(10.dp))
                    Text(
                        text = stringResource(R.string.btn_quiero_colaborar),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

/** Ícono y colores de la tarjeta según la categoría que entrega el backend. */
private data class EstiloPrograma(
    val icono: ImageVector,
    val contenedor: Color,
    val sobreContenedor: Color,
    val acento: Color,
    val sobreAcento: Color
)

@Composable
private fun estiloDe(categoria: CategoriaPrograma): EstiloPrograma {
    val c = MaterialTheme.colorScheme
    return when (categoria) {
        CategoriaPrograma.INCLUSION -> EstiloPrograma(
            Icons.Default.AccountBox,
            c.primaryContainer, c.onPrimaryContainer, c.primary, c.onPrimary
        )
        CategoriaPrograma.PRODUCCION -> EstiloPrograma(
            Icons.Default.Build,
            c.secondaryContainer, c.onSecondaryContainer, c.secondary, c.onSecondary
        )
        CategoriaPrograma.ESCUELA -> EstiloPrograma(
            Icons.Default.Face,
            c.tertiaryContainer, c.onTertiaryContainer, c.tertiary, c.onTertiary
        )
        CategoriaPrograma.OTRO -> EstiloPrograma(
            Icons.Default.Favorite,
            c.surfaceVariant, c.onSurfaceVariant, c.primary, c.onPrimary
        )
    }
}

/** Entrada con fade + deslizamiento vertical, con retardo para escalonar las tarjetas. */
@Composable
private fun TarjetaAnimada(
    visible: Boolean,
    retardo: Int,
    contenido: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(500, delayMillis = retardo)) +
                slideInVertically(tween(500, delayMillis = retardo)) { it / 5 }
    ) {
        contenido()
    }
}

@Composable
private fun TarjetaPrograma(
    icono: ImageVector,
    titulo: String,
    texto: String,
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
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
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
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}