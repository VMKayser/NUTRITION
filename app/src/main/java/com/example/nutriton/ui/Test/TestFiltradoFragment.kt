package com.example.nutriton.ui.Test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriton.R
import com.example.nutriton.data.api.models.Receta
import com.example.nutriton.data.repository.NutritionRepository
import com.example.nutriton.ui.Preferencias.PreferenciasViewModel
import com.example.nutriton.ui.Preferencias.PreferenciasViewModelFactory
import kotlinx.coroutines.launch

/**
 * Fragment de prueba para verificar el filtrado de recetas
 * Este fragment permite probar visualmente que el filtrado por dieta funciona correctamente
 */
class TestFiltradoFragment : Fragment() {

    private lateinit var spinnerTipoDieta: Spinner
    private lateinit var etCaloriasMin: EditText
    private lateinit var etCaloriasMax: EditText
    private lateinit var btnFiltrar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var tvResultados: TextView
    private lateinit var rvRecetas: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: PreferenciasViewModel
    private lateinit var recetasAdapter: TestRecetasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test_filtrado, container, false)
        
        initViews(view)
        setupViewModel()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        return view
    }

    private fun initViews(view: View) {
        spinnerTipoDieta = view.findViewById(R.id.spinner_tipo_dieta_test)
        etCaloriasMin = view.findViewById(R.id.et_calorias_min_test)
        etCaloriasMax = view.findViewById(R.id.et_calorias_max_test)
        btnFiltrar = view.findViewById(R.id.btn_filtrar_test)
        btnLimpiar = view.findViewById(R.id.btn_limpiar_test)
        tvResultados = view.findViewById(R.id.tv_resultados_test)
        rvRecetas = view.findViewById(R.id.rv_recetas_test)
        progressBar = view.findViewById(R.id.progress_bar_test)
    }

    private fun setupViewModel() {
        val repository = NutritionRepository.getInstance(requireContext())
        val factory = PreferenciasViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[PreferenciasViewModel::class.java]
    }

    private fun setupRecyclerView() {
        recetasAdapter = TestRecetasAdapter()
        rvRecetas.apply {
            adapter = recetasAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        // Observar tipos de dieta para llenar el spinner
        viewModel.tiposDieta.observe(viewLifecycleOwner) { tiposDieta ->
            val nombres = listOf("Todas las dietas") + tiposDieta.map { it.nombre }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                nombres
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTipoDieta.adapter = adapter
        }

        // Observar mensajes de error
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
                viewModel.limpiarMensajes()
            }
        }

        // Observar mensajes de éxito
        viewModel.successMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.limpiarMensajes()
            }
        }
    }

    private fun setupClickListeners() {
        btnFiltrar.setOnClickListener {
            aplicarFiltros()
        }

        btnLimpiar.setOnClickListener {
            limpiarFiltros()
        }
    }

    private fun aplicarFiltros() {
        progressBar.visibility = View.VISIBLE
        rvRecetas.visibility = View.GONE
        
        // Obtener valores de los filtros
        val tipoDietaSeleccionado = spinnerTipoDieta.selectedItem?.toString()
        val tipoDieta = if (tipoDietaSeleccionado == "Todas las dietas") null else tipoDietaSeleccionado
        
        val caloriasMinText = etCaloriasMin.text.toString()
        val caloriasMaxText = etCaloriasMax.text.toString()
        
        val caloriasMin = if (caloriasMinText.isNotEmpty()) caloriasMinText.toIntOrNull() else null
        val caloriasMax = if (caloriasMaxText.isNotEmpty()) caloriasMaxText.toIntOrNull() else null

        // Log de debug
        println("DEBUG TestFiltrado: Aplicando filtros:")
        println("  - Tipo de dieta: $tipoDieta")
        println("  - Calorías min: $caloriasMin")
        println("  - Calorías max: $caloriasMax")

        // Llamar al repositorio para obtener recetas filtradas
        val repository = NutritionRepository.getInstance(requireContext())
        
        // Usar corrutinas para la llamada asíncrona
        lifecycleScope.launch {
            try {
                val result = repository.getRecetasFiltradas(
                    categoria = null,
                    tipoDieta = tipoDieta,
                    objetivo = null,
                    caloriasMin = caloriasMin,
                    caloriasMax = caloriasMax
                )
                
                when (result) {
                    is com.example.nutriton.data.api.ApiResult.Success -> {
                        val recetas = result.data
                        requireActivity().runOnUiThread {
                            mostrarResultados(recetas, tipoDieta, caloriasMin, caloriasMax)
                        }
                    }
                    is com.example.nutriton.data.api.ApiResult.Error -> {
                        requireActivity().runOnUiThread {
                            progressBar.visibility = View.GONE
                            Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                    is com.example.nutriton.data.api.ApiResult.Loading -> {
                        // Mantener loading visible
                    }
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun mostrarResultados(recetas: List<Receta>, tipoDieta: String?, caloriasMin: Int?, caloriasMax: Int?) {
        progressBar.visibility = View.GONE
        rvRecetas.visibility = View.VISIBLE

        // Actualizar texto de resultados
        val filtrosTexto = mutableListOf<String>()
        tipoDieta?.let { filtrosTexto.add("Dieta: $it") }
        caloriasMin?.let { filtrosTexto.add("Min: $it cal") }
        caloriasMax?.let { filtrosTexto.add("Max: $it cal") }
        
        val filtrosStr = if (filtrosTexto.isNotEmpty()) " (${filtrosTexto.joinToString(", ")})" else ""
        tvResultados.text = "Resultados: ${recetas.size} recetas encontradas$filtrosStr"

        // Actualizar RecyclerView
        recetasAdapter.updateRecetas(recetas)

        // Log de debug
        println("DEBUG TestFiltrado: Resultados obtenidos:")
        println("  - Total recetas: ${recetas.size}")
        recetas.forEachIndexed { index, receta ->
            val dietasNombres = receta.tipos_dieta?.map { it.nombre } ?: emptyList()
            println("  - Receta $index: ${receta.nombre} | Dietas: $dietasNombres | Calorías: ${receta.informacion_nutricional?.calorias ?: "No disponible"}")
        }
    }

    private fun limpiarFiltros() {
        spinnerTipoDieta.setSelection(0) // "Todas las dietas"
        etCaloriasMin.text.clear()
        etCaloriasMax.text.clear()
        tvResultados.text = "Utiliza los filtros para buscar recetas"
        recetasAdapter.updateRecetas(emptyList())
    }
}

/**
 * Adapter simple para mostrar las recetas en el RecyclerView de prueba
 */
class TestRecetasAdapter : RecyclerView.Adapter<TestRecetasAdapter.RecetaViewHolder>() {
    
    private var recetas: List<Receta> = emptyList()

    fun updateRecetas(nuevasRecetas: List<Receta>) {
        recetas = nuevasRecetas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return RecetaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        holder.bind(recetas[position])
    }

    override fun getItemCount(): Int = recetas.size

    class RecetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titulo: TextView = itemView.findViewById(android.R.id.text1)
        private val subtitulo: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(receta: Receta) {
            titulo.text = receta.nombre
            
            val dietasNombres = receta.tipos_dieta?.map { it.nombre }?.joinToString(", ") ?: "Sin dieta"
            val calorias = receta.informacion_nutricional?.calorias ?: "No disponible"
            subtitulo.text = "Dietas: $dietasNombres | Calorías: $calorias"
        }
    }
}
