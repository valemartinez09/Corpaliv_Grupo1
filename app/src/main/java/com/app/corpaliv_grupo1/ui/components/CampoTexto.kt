package com.app.corpaliv_grupo1.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

/** Campo reutilizable: ícono inicial, ícono de advertencia y mensaje cuando hay error. */
@Composable
fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    @StringRes etiqueta: Int,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    @StringRes error: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(etiqueta)) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        trailingIcon = if (error != null) {
            {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        } else null,
        isError = error != null,
        supportingText = if (error != null) {
            { Text(stringResource(error)) }
        } else null,
        singleLine = minLines == 1,
        minLines = minLines,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = keyboardActions
    )
}