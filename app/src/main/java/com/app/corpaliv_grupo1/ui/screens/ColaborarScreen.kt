package com.app.corpaliv_grupo1.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.model.TipoColaborador
import com.app.corpaliv_grupo1.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColaborarScreen(navController: NavController) {
    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.colaborar_titulo)) },
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
                    text = stringResource(R.string.colaborar_pregunta),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                OpcionTipo(
                    icono = Icons.Default.Person,
                    titulo = R.string.colaborar_persona,
                    descripcion = R.string.colaborar_persona_desc,
                    contenedor = colores.primaryContainer,
                    sobreContenedor = colores.onPrimaryContainer,
                    acento = colores.primary,
                    sobreAcento = colores.onPrimary,
                    onClick = {
                        navController.navigate(Routes.AportePersona.route)
                    }
                )
                OpcionTipo(
                    icono = Icons.Default.AccountBox,
                    titulo = R.string.colaborar_empresa,
                    descripcion = R.string.colaborar_empresa_desc,
                    contenedor = colores.secondaryContainer,
                    sobreContenedor = colores.onSecondaryContainer,
                    acento = colores.secondary,
                    sobreAcento = colores.onSecondary,
                    onClick = {
                        navController.navigate(Routes.Formulario.crear(TipoColaborador.EMPRESA))
                    }
                )
            }
        }
    }
}

@Composable
private fun OpcionTipo(
    icono: ImageVector,
    @StringRes titulo: Int,
    @StringRes descripcion: Int,
    contenedor: Color,
    sobreContenedor: Color,
    acento: Color,
    sobreAcento: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = contenedor,
            contentColor = sobreContenedor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = acento,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = sobreAcento,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(titulo),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(descripcion),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}