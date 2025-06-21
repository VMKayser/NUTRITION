// Este archivo ya no se usa - se mantiene el DetallePlanComidasFragment con el diseño original
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutriton.data.repository.NutritionRepository
import com.example.nutriton.databinding.FragmentRecetasPlanBinding
import com.example.nutriton.ui.PlanComidas.RecetasPorPlanViewModel
import com.example.nutriton.ui.recetas.RecetasAdapter

class RecetasPorPlanFragment : Fragment() {

    private var _binding: FragmentRecetasPlanBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: RecetasPorPlanViewModel
    private lateinit var recetasAdapter: RecetasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecetasPlanBinding.inflate(inflater, container, false)

        // Inicializar ViewModel con Repository
        val repository = NutritionRepository.getInstance(requireContext())
        viewModel = ViewModelProvider(
            this,
            RecetasPorPlanViewModelFactory(repository)
        )[RecetasPorPlanViewModel::class.java]
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        // Obtener argumentos del Bundle
        val tipoDietaNombre = arguments?.getString("tipoDietaNombre")
        val planNombre = arguments?.getString("planNombre")
        
        if (tipoDietaNombre != null) {
            viewModel.cargarRecetasPorPlan(tipoDietaNombre, planNombre)
        } else {
            Toast.makeText(context, "Error: Tipo de dieta no especificado", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }
    }    private fun setupRecyclerView() {
        recetasAdapter = RecetasAdapter { receta ->
            // Navegar al detalle de la receta
            val bundle = Bundle().apply {
                putInt("recetaId", receta.id)
            }
            findNavController().navigate(
                com.example.nutriton.R.id.action_recetasPorPlan_to_detalleReceta,
                bundle
            )
        }
        
        binding.rvRecetas.apply {
            adapter = recetasAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        viewModel.recetas.observe(viewLifecycleOwner) { recetas ->
            recetasAdapter.submitList(recetas)
            
            // Mostrar mensaje si no hay recetas
            if (recetas.isEmpty() && viewModel.isLoading.value != true) {
                binding.tvNoRecetas.visibility = View.VISIBLE
                binding.rvRecetas.visibility = View.GONE
            } else {
                binding.tvNoRecetas.visibility = View.GONE
                binding.rvRecetas.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvRecetas.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.limpiarError()
            }
        }

        viewModel.planNombre.observe(viewLifecycleOwner) { nombre ->
            binding.tvTituloPlan.text = "Recetas de $nombre"
        }
    }    private fun setupClickListeners() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnRecargar.setOnClickListener {
            val tipoDietaNombre = arguments?.getString("tipoDietaNombre")
            val planNombre = arguments?.getString("planNombre")
            if (tipoDietaNombre != null) {
                viewModel.recargarRecetas(tipoDietaNombre, planNombre)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// ViewModelFactory para inyectar el Repository
class RecetasPorPlanViewModelFactory(
    private val repository: NutritionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecetasPorPlanViewModel::class.java)) {
            return RecetasPorPlanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}