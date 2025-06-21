package com.example.nutriton.ui.PlanComidas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutriton.R
import com.example.nutriton.databinding.FragmentDetallePlanComidasBinding
import com.example.nutriton.ui.PlanComidas.adapter.ComidasPlanAdapter
import com.example.nutriton.ui.PlanComidas.model.ComidaCompleta
import com.example.nutriton.ui.PlanComidas.model.PlanComida
import com.example.nutriton.ui.PlanComidas.model.TipoDieta

class DetallePlanComidasFragment : Fragment() {

    private var _binding: FragmentDetallePlanComidasBinding? = null
    private val binding get() = _binding!!
    private lateinit var detallePlanViewModel: DetallePlanComidasViewModel
    
    private lateinit var desayunoAdapter: ComidasPlanAdapter
    private lateinit var almuerzoAdapter: ComidasPlanAdapter
    private lateinit var cenaAdapter: ComidasPlanAdapter
    
    // Referencias a los botones de días
    private lateinit var diaButtons: List<TextView>
    private var diaSeleccionado = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detallePlanViewModel = ViewModelProvider(this)[DetallePlanComidasViewModel::class.java]
        _binding = FragmentDetallePlanComidasBinding.inflate(inflater, container, false)
        val root: View = binding.root        // Obtener argumentos del bundle
        arguments?.let { args ->
            val planId = args.getString("planId")
            val planNombre = args.getString("planNombre")
            val planDescripcion = args.getString("planDescripcion")
            val planImagenUrl = args.getString("planImagenUrl")
            val tipoDietaNombre = args.getString("tipoDietaNombre") // Obtener tipo de dieta
            
            if (planId != null && planNombre != null && planDescripcion != null) {
                val tipoDieta = tipoDietaNombre?.let { 
                    TipoDieta(
                        id = planId, // Usar el mismo ID por ahora
                        nombre = it,
                        descripcion = ""
                    )
                }
                
                val plan = PlanComida(
                    id = planId,
                    nombre = planNombre,
                    descripcion = planDescripcion,
                    imagenUrl = planImagenUrl ?: "",
                    categoria = "Plan Nutricional",
                    duracion = "7 días",
                    calorias = 2000,
                    tipoDieta = tipoDieta
                )
                detallePlanViewModel.setPlan(plan)
            }
        }

        setupDayNavigation()
        setupRecyclerViews()
        setupObservers()
        setupClickListeners()
        
        return root
    }    fun setupDayNavigation() {
        // Inicializar referencias a los botones de días
        diaButtons = listOf(
            binding.btnDia1,
            binding.btnDia2,
            binding.btnDia3,
            binding.btnDia4,
            binding.btnDia5
        )
        
        // Configurar listeners para cada día
        diaButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                selectDay(index + 1)
            }
        }
        
        // Seleccionar día 1 por defecto
        selectDay(1)
    }
      fun selectDay(day: Int) {
        // Evitar actualizaciones innecesarias
        if (diaSeleccionado == day) return
        
        diaSeleccionado = day
        
        // Actualizar UI de los botones
        diaButtons.forEachIndexed { index, button ->
            if (index + 1 == day) {
                // Día seleccionado
                button.setBackgroundResource(R.drawable.dia_selector_active)
                button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            } else {
                // Día no seleccionado
                button.setBackgroundResource(R.drawable.dia_selector_inactive)
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
            }
        }
        
        // Solo notificar al ViewModel si es una selección del usuario
        detallePlanViewModel.selectDia(day)
    }

    fun setupRecyclerViews() {
        // Adapter para desayuno
        desayunoAdapter = ComidasPlanAdapter { comida ->
            onComidaSelected(comida)
        }
        binding.rvDesayuno.apply {
            adapter = desayunoAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }

        // Adapter para almuerzo
        almuerzoAdapter = ComidasPlanAdapter { comida ->
            onComidaSelected(comida)
        }
        binding.rvAlmuerzo.apply {
            adapter = almuerzoAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }

        // Adapter para cena
        cenaAdapter = ComidasPlanAdapter { comida ->
            onComidaSelected(comida)
        }
        binding.rvCena.apply {
            adapter = cenaAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }
    }    fun setupObservers() {
        // Observar estado de carga y datos
        detallePlanViewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

            state.error?.let { error ->
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }        // Observar comidas por categoría
        detallePlanViewModel.comidasDesayuno.observe(viewLifecycleOwner) { comidas ->
            println("DEBUG Fragment: comidasDesayuno observer - recibidas ${comidas.size} comidas")
            comidas.forEach { comida ->
                println("DEBUG Fragment: Desayuno - ${comida.nombre}, id: ${comida.id}")
            }
            desayunoAdapter.submitList(comidas)
            binding.sectionDesayuno.visibility = if (comidas.isNotEmpty()) View.VISIBLE else View.GONE
        }

        detallePlanViewModel.comidasAlmuerzo.observe(viewLifecycleOwner) { comidas ->
            println("DEBUG Fragment: comidasAlmuerzo observer - recibidas ${comidas.size} comidas")
            comidas.forEach { comida ->
                println("DEBUG Fragment: Almuerzo - ${comida.nombre}, id: ${comida.id}")
            }
            almuerzoAdapter.submitList(comidas)
            binding.sectionAlmuerzo.visibility = if (comidas.isNotEmpty()) View.VISIBLE else View.GONE
        }

        detallePlanViewModel.comidasCena.observe(viewLifecycleOwner) { comidas ->
            println("DEBUG Fragment: comidasCena observer - recibidas ${comidas.size} comidas")
            comidas.forEach { comida ->
                println("DEBUG Fragment: Cena - ${comida.nombre}, id: ${comida.id}")
            }
            cenaAdapter.submitList(comidas)
            binding.sectionCena.visibility = if (comidas.isNotEmpty()) View.VISIBLE else View.GONE
        }

        // Observar plan actual
        detallePlanViewModel.planActual.observe(viewLifecycleOwner) { plan ->
            plan?.let {
                // Si tienes elementos de UI para mostrar el título y descripción del plan
                // puedes descomentarlos cuando tengas los IDs correctos
                // binding.tvTituloPlan.text = it.nombre
                // binding.tvDescripcionPlan.text = it.descripcion
            }
        }
    }

    fun setupClickListeners() {
        // TODO: Configurar navegación hacia atrás cuando esté lista
        // binding.ivBack?.setOnClickListener {
        //     requireActivity().onBackPressed()
        // }
    }    fun onComidaSelected(comida: ComidaCompleta) {
        // Navegar al detalle de la receta
        try {
            val bundle = Bundle().apply {
                putInt("recetaId", comida.id)
            }
            findNavController().navigate(
                R.id.action_detalleplan_to_detallereceta,
                bundle
            )
        } catch (e: Exception) {
            Toast.makeText(context, "Error al navegar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(tipoDietaId: Int): DetallePlanComidasFragment {
            val fragment = DetallePlanComidasFragment()
            val args = Bundle().apply {
                putInt("tipo_dieta_id", tipoDietaId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
