package com.example.nutriton.ui.inicio

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nutriton.databinding.FragmentInicioBinding

class InicioFragment1 : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inicioViewModel =
            ViewModelProvider(this).get(InicioViewModel::class.java)

        // Rota la imagen solo al iniciar el fragmento
        try {
            val prefs = requireContext().getSharedPreferences("image_prefs", Context.MODE_PRIVATE)
            val imageNames = listOf("mulos", "calabacines", "pasta", "sopa", "sopajuliana")
            val lastIndex = prefs.getInt("last_image_index", -1)
            val nextIndex = (lastIndex + 1) % imageNames.size
            prefs.edit().putInt("last_image_index", nextIndex).apply()
        } catch (e: Exception) {
            // Si falla, continúa sin crash
        }

        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        try {
            val textView: TextView = binding.textInicio
            inicioViewModel.text.observe(viewLifecycleOwner) {
                textView.text = it
            }
        } catch (e: Exception) {
            // Si falla, usa texto por defecto
        }

        setupImageFromPrefs()
        // Comentamos Compose temporalmente
        // setupComposeGraph()
        
        return root
    }

    private fun setupImageFromPrefs() {
        try {
            val imageView = binding.imageView2
            val textNombrePlato = binding.textNombrePlato
            val (imageResId, imageName, index) = getImageResIdAndNameFromPrefs(requireContext())
            val prettyNames = listOf(
                "Mulos de Pollo",
                "Calabacines Rellenos", 
                "Pasta Integral",
                "Sopa de Verduras",
                "Sopa Juliana"
            )
            
            // Usar un drawable predeterminado si no se encuentra la imagen
            if (imageResId != 0) {
                try {
                    imageView.background = requireContext().getDrawable(imageResId)
                } catch (e: Exception) {
                    // Si falla, usar un color por defecto
                    imageView.setBackgroundColor(requireContext().getColor(android.R.color.holo_blue_light))
                }
            } else {
                // Si no se encuentra la imagen, usar un color por defecto
                imageView.setBackgroundColor(requireContext().getColor(android.R.color.holo_blue_light))
            }
            
            // Mostrar el nombre bonito del plato
            textNombrePlato.text = prettyNames.getOrNull(index) ?: imageName.replaceFirstChar { it.uppercase() }
        } catch (e: Exception) {
            // Si falla, continúa sin crash
        }
    }
    
    private fun getImageResIdAndNameFromPrefs(context: Context): Triple<Int, String, Int> {
        return try {
            val prefs = context.getSharedPreferences("image_prefs", Context.MODE_PRIVATE)
            val imageNames = listOf(
                "mulos",
                "calabacines", 
                "pasta",
                "sopa",
                "sopajuliana"
            )
            val index = prefs.getInt("last_image_index", 0).coerceIn(0, imageNames.lastIndex)
            val imageName = imageNames[index]
            val resId = try {
                context.resources.getIdentifier(imageName, "drawable", context.packageName)
            } catch (e: Exception) {
                0
            }
            Triple(resId, imageName, index)
        } catch (e: Exception) {
            Triple(0, "default", 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
