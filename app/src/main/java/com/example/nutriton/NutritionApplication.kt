package com.example.nutriton

import android.app.Application

class NutritionApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Aquí podrías inicializar componentes globales como Room database
        // si decides implementarlo en el futuro
    }
}
