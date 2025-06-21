package com.example.nutriton

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nutriton.databinding.ActivityMainBinding
import androidx.navigation.fragment.NavHostFragment
import com.example.nutriton.utils.DatabaseInitializer
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Para controlar el modo oscuro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Rota la imagen solo al iniciar la app (no al navegar entre fragmentos)
        val prefs = getSharedPreferences("image_prefs", MODE_PRIVATE)
        val imageNames = listOf("mulos", "calabacines", "pasta", "sopa", "sopajuliana")
        val lastIndex = prefs.getInt("last_image_index", -1)
        val nextIndex = (lastIndex + 1) % imageNames.size
        prefs.edit().putInt("last_image_index", nextIndex).apply()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar base de datos
        inicializarBaseDatos()

        val navView: BottomNavigationView = binding.navView

        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as? NavHostFragment)?.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_inicio, R.id.navigation_PlanComidas, R.id.navigation_Progreso, R.id.navigation_Perfil
            )
        )
        if (navController != null) {
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }
    
    private fun inicializarBaseDatos() {
        lifecycleScope.launch {
            DatabaseInitializer.initializeDatabase(this@MainActivity)
        }
    }
}