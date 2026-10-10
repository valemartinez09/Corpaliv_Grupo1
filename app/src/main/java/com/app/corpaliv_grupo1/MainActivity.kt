package com.app.corpaliv_grupo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.app.corpaliv_grupo1.ui.HomeScreen
import com.app.corpaliv_grupo1.ui.theme.Corpaliv_Grupo1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // El tema aplica los colores y tipografías de MaterialTheme
            Corpaliv_Grupo1Theme {
                // HomeScreen ya trae su propio Scaffold, así que no se necesita otro aquí
                HomeScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    Corpaliv_Grupo1Theme {
        HomeScreen()
    }
}