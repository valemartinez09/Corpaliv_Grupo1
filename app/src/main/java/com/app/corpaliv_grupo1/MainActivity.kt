package com.app.corpaliv_grupo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.app.corpaliv_grupo1.navigation.AppNavigation
import com.app.corpaliv_grupo1.ui.screens.HomeScreenAdaptativa
import com.app.corpaliv_grupo1.ui.theme.Corpaliv_Grupo1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Corpaliv_Grupo1Theme {
                // Inicia el flujo de navegación de toda la app
                AppNavigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    Corpaliv_Grupo1Theme {
        HomeScreenAdaptativa()
    }
}