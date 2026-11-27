package com.app.corpaliv_grupo1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.data.remote.dto.RegistroResponse
import com.app.corpaliv_grupo1.model.EstadoRegistro
import com.app.corpaliv_grupo1.model.TipoColaborador
import com.app.corpaliv_grupo1.navigation.Routes
import com.app.corpaliv_grupo1.viewmodel.PanelFuncionarioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Panel del funcionario: lista los registros de interés que entrega registros-service. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelFuncionarioScreen(
    navController: NavController,
    viewModel: PanelFuncionarioViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    // Registro pendiente de confirmar eliminación (null = sin diálogo)
    var porEliminar by remember { mutableStateOf<RegistroResponse?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val mensajeErrorAccion = stringResource(R.string.panel_error_accion)
    LaunchedEffect(estado.errorAccion) {
        if (estado.errorAccion) {
            snackbarHostState.showSnackbar(mensajeErrorAccion)
            viewModel.descartarErrorAccion()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.panel_titulo)) },
                actions = {
                    IconButton(onClick = viewModel::cargar, enabled = !estado.cargando) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.cd_actualizar)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        estado.cargando -> CircularProgressIndicator()

                        estado.errorCarga -> Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.panel_error_carga),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Button(onClick = viewModel::cargar) {
                                Text(stringResource(R.string.panel_reintentar))
                            }
                        }

                        estado.registros.isEmpty() -> Text(
                            text = stringResource(R.string.panel_vacio),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )

                        else -> Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = stringResource(R.string.panel_total, estado.registros.size),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            LazyColumn(
                                contentPadding = PaddingValues(bottom = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(estado.registros, key = { it.id }) { registro ->
                                    RegistroCard(
                                        registro = registro,
                                        procesando = estado.procesandoId == registro.id,
                                        onContactado = { viewModel.marcarContactado(registro.id) },
                                        onEliminar = { porEliminar = registro }
                                    )
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        // Cerrar sesión: vuelve a Inicio y limpia el historial
                        navController.navigate(Routes.Inicio.route) {
                            popUpTo(Routes.Inicio.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
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

    porEliminar?.let { registro ->
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            title = { Text(stringResource(R.string.dialogo_eliminar_titulo)) },
            text = { Text(stringResource(R.string.dialogo_eliminar_texto)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminar(registro.id)
                    porEliminar = null
                }) { Text(stringResource(R.string.btn_eliminar)) }
            },
            dismissButton = {
                TextButton(onClick = { porEliminar = null }) {
                    Text(stringResource(R.string.btn_cancelar))
                }
            }
        )
    }
}

@Composable
private fun RegistroCard(
    registro: RegistroResponse,
    procesando: Boolean,
    onContactado: () -> Unit,
    onEliminar: () -> Unit
) {
    val fecha = remember(registro.fecha) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL")).format(Date(registro.fecha))
    }
    val tipoTexto = stringResource(
        if (registro.tipo == TipoColaborador.EMPRESA) R.string.valor_empresa
        else R.string.valor_persona
    )
    val contactado = registro.estado == EstadoRegistro.CONTACTADO

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 4.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = tipoTexto,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = fecha, style = MaterialTheme.typography.labelSmall)
                }
                IconButton(onClick = onEliminar, enabled = !procesando) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.cd_eliminar_registro)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = registro.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    EtiquetaEstado(contactado = contactado)
                }
                if (!registro.contacto.isNullOrBlank()) {
                    Text(stringResource(R.string.panel_contacto, registro.contacto))
                }
                Text(stringResource(R.string.panel_correo, registro.correo))
                Text(
                    text = stringResource(registro.modalidad.etiqueta),
                    fontWeight = FontWeight.SemiBold
                )
                registro.frecuencia?.let {
                    Text(stringResource(R.string.formulario_aporte, stringResource(it.etiqueta)))
                }
                if (!registro.mensaje.isNullOrBlank()) {
                    Text(stringResource(R.string.panel_mensaje, registro.mensaje))
                }

                if (!contactado) {
                    Spacer(Modifier.height(8.dp))
                    FilledTonalButton(
                        onClick = onContactado,
                        enabled = !procesando,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (procesando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                        Spacer(Modifier.size(8.dp))
                        Text(stringResource(R.string.panel_marcar_contactado))
                    }
                }
            }
        }
    }
}

@Composable
private fun EtiquetaEstado(contactado: Boolean) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (contactado) MaterialTheme.colorScheme.tertiaryContainer
        else MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = stringResource(
                if (contactado) R.string.panel_estado_contactado
                else R.string.panel_estado_pendiente
            ),
            style = MaterialTheme.typography.labelMedium,
            color = if (contactado) MaterialTheme.colorScheme.onTertiaryContainer
            else MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}