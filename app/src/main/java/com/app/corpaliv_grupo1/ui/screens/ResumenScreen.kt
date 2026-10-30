package com.app.corpaliv_grupo1.ui.screens

import com.app.corpaliv_grupo1.model.FormularioUiState
import com.app.corpaliv_grupo1.model.TipoColaborador
import com.app.corpaliv_grupo1.ui.theme.ExitoVerde
import androidx.annotation.StringRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.corpaliv_grupo1.R


@Composable
fun ResumenScreen(
    estado: FormularioUiState,
    onVolver: () -> Unit
) {
    var aparecer by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { aparecer = true }
    val escala by animateFloatAsState(
        targetValue = if (aparecer) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaExito"
    )

    val esEmpresa = estado.tipo == TipoColaborador.EMPRESA

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ExitoVerde,
                modifier = Modifier
                    .size(88.dp)
                    .scale(escala)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.exito_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.exito_mensaje),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.resumen_titulo),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    FilaResumen(
                        etiqueta = R.string.resumen_tipo,
                        valor = stringResource(
                            if (esEmpresa) R.string.valor_empresa else R.string.valor_persona
                        )
                    )
                    FilaResumen(
                        etiqueta = if (esEmpresa) R.string.label_nombre_empresa
                        else R.string.label_nombre_persona,
                        valor = estado.nombre.trim()
                    )
                    if (esEmpresa) {
                        FilaResumen(
                            etiqueta = R.string.label_contacto,
                            valor = estado.contacto.trim()
                        )
                    }
                    FilaResumen(
                        etiqueta = R.string.label_correo,
                        valor = estado.correo.trim()
                    )
                    estado.modalidad?.let {
                        FilaResumen(
                            etiqueta = R.string.resumen_modalidad,
                            valor = stringResource(it.etiqueta)
                        )
                    }
                    estado.frecuencia?.let {
                        FilaResumen(
                            etiqueta = R.string.resumen_frecuencia,
                            valor = stringResource(it.etiqueta)
                        )
                    }
                    estado.carta?.let {
                        FilaResumen(
                            etiqueta = R.string.resumen_carta,
                            valor = it.nombre
                        )
                    }
                    if (estado.mensaje.isNotBlank()) {
                        FilaResumen(
                            etiqueta = R.string.resumen_mensaje,
                            valor = estado.mensaje.trim()
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_volver_inicio),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun FilaResumen(@StringRes etiqueta: Int, valor: String) {
    Column {
        Text(
            text = stringResource(etiqueta),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}