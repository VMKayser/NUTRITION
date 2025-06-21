package com.example.nutriton.ui.inicio

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.nutriton.R
import com.example.nutriton.databinding.FragmentInicioBinding
import com.example.nutriton.graphics.PieScreen
import com.example.nutriton.data.repository.NutritionRepository
import com.example.nutriton.database.NutritionDatabase


class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Configurar el ViewModel con el repositorio

        val repository = NutritionRepository.getInstance(requireContext())

        val viewModelFactory = InicioViewModelFactory(repository)
        val inicioViewModel = ViewModelProvider(this, viewModelFactory)[InicioViewModel::class.java]

        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observar el texto del título
        try {
            val textView: TextView = binding.textInicio
            inicioViewModel.text.observe(viewLifecycleOwner) {
                textView.text = it
            }
        } catch (e: Exception) {
            // Si falla, usa texto por defecto
        }

        // Observar la receta del día
        inicioViewModel.recetaDelDia.observe(viewLifecycleOwner) { receta ->
            if (receta != null) {
                setupRecetaDelDia(receta)
            }
        }

        // Observar errores
        inicioViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                // Si hay error, usar imágenes por defecto
                setupImageFromPrefs()
            }
        }

        // Observar estado de carga
        inicioViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Aquí puedes mostrar un indicador de carga si lo deseas
            if (isLoading) {
                // Mostrar imágenes por defecto mientras carga
                setupImageFromPrefs()
            }
        }

        // Habilitamos Compose para el gráfico
        setupComposeGraph()

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
            textNombrePlato.text =
                prettyNames.getOrNull(index) ?: imageName.replaceFirstChar { it.uppercase() }
        } catch (e: Exception) {
            // Si falla, continúa sin crash
        }
    }

    private fun setupComposeGraph() {
        try {
            val composeView = binding.composeGraph
            composeView.setContent {
                PieScreen()
            }
        } catch (e: Exception) {
            // Si falla Compose, continúa sin crash
        }
    }

    private fun getImageResIdAndNameFromPrefs(context: Context): Triple<Int, String, Int> {
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
        return Triple(resId, imageName, index)
    }

    private fun setupRecetaDelDia(receta: com.example.nutriton.data.api.models.Receta) {
        try {
            val imageView = binding.imageView2
            val textNombrePlato = binding.textNombrePlato

            // Mostrar el nombre de la receta
            textNombrePlato.text = receta.nombre

            // Cargar la imagen de la receta usando Glide
            if (!receta.imagen_url.isNullOrEmpty()) {
                Glide.with(this)
                    .load(receta.imagen_url)
                    .placeholder(R.drawable.ic_launcher_background) // Placeholder mientras carga
                    .error(R.drawable.ic_launcher_foreground) // Imagen de error
                    .into(imageView)
            } else {
                // Si no hay imagen, usar una imagen por defecto
                imageView.setImageResource(R.drawable.ic_launcher_background)
            }

            // Configurar click listener para navegar al detalle
            imageView.setOnClickListener {
                navigateToRecetaDetalle(receta.id)
            }

        } catch (e: Exception) {
            // Si falla, usar método por defecto
            setupImageFromPrefs()
        }
    }

    private fun navigateToRecetaDetalle(recetaId: Int) {
        try {
            // Navegar al DetalleRecetaFragment pasando el ID de la receta
            val bundle = Bundle().apply {
                putInt("recetaId", recetaId)
            }
            findNavController().navigate(R.id.action_inicio_to_detallereceta, bundle)
        } catch (e: Exception) {
            // Si falla la navegación, no hacer nada
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}