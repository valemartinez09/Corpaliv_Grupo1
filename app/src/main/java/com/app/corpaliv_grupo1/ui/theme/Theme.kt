package com.app.corpaliv_grupo1.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = MoradoCorpaliv,
    onPrimary = Color.White,
    primaryContainer = MoradoClaro,
    onPrimaryContainer = MoradoOscuro,
    secondary = NaranjaCorpaliv,
    onSecondary = GrisCorpaliv,
    secondaryContainer = NaranjaClaro,
    onSecondaryContainer = NaranjaOscuro,
    tertiary = RosaCorpaliv,
    onTertiary = Color.White,
    tertiaryContainer = RosaClaro,
    onTertiaryContainer = RosaOscuro,
    background = FondoClaro,
    onBackground = GrisCorpaliv,
    surface = FondoClaro,
    onSurface = GrisCorpaliv,
    surfaceVariant = SuperficieVariante,
    onSurfaceVariant = TextoSecundario,
    outline = Contorno,
    error = ErrorRojo,
    onError = Color.White,
    errorContainer = ErrorContenedor,
    onErrorContainer = ErrorSobreContenedor
)

private val EsquemaOscuro = darkColorScheme(
    primary = MoradoSuave,
    onPrimary = MoradoOscuro,
    primaryContainer = MoradoCorpaliv,
    onPrimaryContainer = MoradoClaro,
    secondary = NaranjaCorpaliv,
    onSecondary = NaranjaOscuro,
    secondaryContainer = NaranjaProfundo,
    onSecondaryContainer = NaranjaClaro,
    tertiary = RosaSuave,
    onTertiary = RosaOscuro,
    tertiaryContainer = RosaProfundo,
    onTertiaryContainer = RosaClaro,
    background = FondoOscuro,
    onBackground = TextoClaro,
    surface = FondoOscuro,
    onSurface = TextoClaro,
    surfaceVariant = SuperficieVarianteOscura,
    onSurfaceVariant = TextoSecundarioOscuro,
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = ErrorContenedor
)

@Composable
fun Corpaliv_Grupo1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Sin color dinámico a propósito: la app mantiene la identidad del logo.
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}