package com.app.corpaliv_grupo1.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.model.EstadoEnvio
import com.app.corpaliv_grupo1.model.ModalidadColaboracion
import com.app.corpaliv_grupo1.model.TipoColaborador
import com.app.corpaliv_grupo1.navigation.Routes
import com.app.corpaliv_grupo1.ui.components.CampoTexto
import com.app.corpaliv_grupo1.viewmodel.FormularioViewModel

/**
 * Formulario de interés de colaboración.
 * El tipo (PERSONA / EMPRESA) llega por la ruta y lo lee el FormularioViewModel.
 * Al terminar el envío se reemplaza con una animación por la pantalla de resumen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    navController: NavController,
    viewModel: FormularioViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val esEmpresa = estado.tipo == TipoColaborador.EMPRESA
    val enviando = estado.envio == EstadoEnvio.ENVIANDO
    val enviado = estado.envio == EstadoEnvio.ENVIADO

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (esEmpresa) R.string.formulario_titulo_empresa
                            else R.string.formulario_titulo_persona
                        )
                    )
                },
                navigationIcon = {
                    // Una vez enviado no tiene sentido volver al formulario
                    if (!enviado) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.cd_volver)
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Crossfade(
            targetState = enviado,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            label = "formularioAResumen"
        ) { mostrarResumen ->
            if (mostrarResumen) {
                ResumenScreen(
                    estado = estado,
                    onVolver = {
                        navController.popBackStack(Routes.Inicio.route, inclusive = false)
                    }
                )
            } else {
                ContenidoFormulario(
                    viewModel = viewModel,
                    esEmpresa = esEmpresa,
                    enviando = enviando
                )
            }
        }
    }
}

@Composable
private fun ContenidoFormulario(
    viewModel: FormularioViewModel,
    esEmpresa: Boolean,
    enviando: Boolean
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val foco = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 640.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Aviso: el caso prohíbe datos personales reales
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.formulario_aviso_demo),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            CampoTexto(
                valor = estado.nombre,
                onCambio = viewModel::onNombreChange,
                etiqueta = if (esEmpresa) R.string.label_nombre_empresa
                else R.string.label_nombre_persona,
                icono = if (esEmpresa) Icons.Default.AccountBox else Icons.Default.Person,
                error = estado.errores.nombre
            )

            // Solo las empresas indican una persona de contacto
            if (esEmpresa) {
                CampoTexto(
                    valor = estado.contacto,
                    onCambio = viewModel::onContactoChange,
                    etiqueta = R.string.label_contacto,
                    icono = Icons.Default.Person,
                    error = estado.errores.contacto
                )
            }

            CampoTexto(
                valor = estado.correo,
                onCambio = viewModel::onCorreoChange,
                etiqueta = R.string.label_correo,
                icono = Icons.Default.Email,
                error = estado.errores.correo,
                keyboardType = KeyboardType.Email
            )

            val frecuencia = estado.frecuencia
            // Sin frecuencia, una persona elige entre las otras formas de ayudar (no donación)
            val modalidades = if (estado.tipo == TipoColaborador.PERSONA) {
                estado.tipo.modalidades.filter { it != ModalidadColaboracion.DONACION }
            } else {
                estado.tipo.modalidades
            }

            if (frecuencia != null) {
                // Aporte ya elegido en la pantalla anterior
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(
                        text = stringResource(
                            R.string.formulario_aporte,
                            stringResource(frecuencia.etiqueta)
                        ),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.label_modalidad),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                modalidades.forEach { modalidad ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .selectable(
                                selected = estado.modalidad == modalidad,
                                onClick = { viewModel.onModalidadChange(modalidad) },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // onClick = null: el clic lo maneja toda la fila (mejor accesibilidad)
                        RadioButton(
                            selected = estado.modalidad == modalidad,
                            onClick = null
                        )
                        Text(
                            text = stringResource(modalidad.etiqueta),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
                estado.errores.modalidad?.let {
                    Text(
                        text = stringResource(it),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            CampoTexto(
                valor = estado.mensaje,
                onCambio = viewModel::onMensajeChange,
                etiqueta = R.string.label_mensaje,
                icono = Icons.Default.Create,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { foco.clearFocus() }),
                minLines = 3
            )

            // Aceptación de contacto
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .toggleable(
                            value = estado.aceptaTerminos,
                            onValueChange = viewModel::onTerminosChange,
                            role = Role.Checkbox
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = estado.aceptaTerminos,
                        onCheckedChange = null
                    )
                    Text(
                        text = stringResource(R.string.formulario_terminos),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
                estado.errores.terminos?.let {
                    Text(
                        text = stringResource(it),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Error si no se pudo guardar en la base local
            if (estado.errorGuardado) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(
                        text = stringResource(R.string.error_guardado),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = {
                    foco.clearFocus()
                    viewModel.enviar()
                },
                enabled = !enviando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (enviando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = stringResource(R.string.enviando),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                } else {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Text(
                        text = stringResource(R.string.formulario_enviar),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}