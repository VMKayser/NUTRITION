package com.example.nutriton.graphics

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class Utils {
    fun colorAleatorio(): Color {
        val colors = listOf(
            Color(0xFFFF5252), // Rojo brillante
            Color(0xFFFFC107), // Amarillo vibrante
            Color(0xFF40C4FF), // Azul celeste
            Color(0xFF69F0AE), // Verde lima
            Color(0xFFFF6D00), // Naranja intenso
            Color(0xFFE040FB)  // Violeta brillante
        )
        val randomNumber = (Math.random() * colors.size).toInt()
        return colors[randomNumber]
    }

    fun colorPlateado(): Color {
        return Color(0xFFC0C0C0) // Plateado
    }
}
