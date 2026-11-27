package com.app.corpaliv_grupo1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.model.IndicadoresUiState
import com.app.corpaliv_grupo1.viewmodel.IndicadoresViewModel
import java.util.Locale

@Composable
fun TarjetaIndicadores(
    modifier: Modifier = Modifier,
    viewModel: IndicadoresViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.indicadores_titulo),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            when (val e = estado) {
                IndicadoresUiState.Cargando -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Text(
                            text = stringResource(R.string.indicadores_cargando),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                is IndicadoresUiState.Exito -> {
                    e.indicadores.forEach { indicador ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = indicador.nombre,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = formatearValor(indicador.valor),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    val fecha = e.indicadores.firstOrNull()?.fecha.orEmpty()
                    if (fecha.isNotBlank()) {
                        Text(
                            text = stringResource(R.string.indicadores_fecha, fecha),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Text(
                        text = stringResource(R.string.indicadores_fuente),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                IndicadoresUiState.Fallo -> {
                    Text(
                        text = stringResource(R.string.indicadores_error),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    TextButton(onClick = { viewModel.cargar() }) {
                        Text(stringResource(R.string.indicadores_reintentar))
                    }
                }
            }
        }
    }
}

private fun formatearValor(valor: Double): String =
    String.format(Locale.forLanguageTag("es-CL"), "\$%,.2f", valor)