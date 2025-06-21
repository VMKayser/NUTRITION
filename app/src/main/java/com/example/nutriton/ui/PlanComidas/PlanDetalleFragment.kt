package com.example.nutriton.ui.PlanComidas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nutriton.databinding.FragmentPlanDetalleBinding
import com.example.nutriton.ui.PlanComidas.model.PlanComida

class PlanDetalleFragment : Fragment() {

    private var _binding: FragmentPlanDetalleBinding? = null
    private val binding get() = _binding!!
    private lateinit var planComidasViewModel: PlanComidasViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        planComidasViewModel = ViewModelProvider(requireActivity())[PlanComidasViewModel::class.java]
        _binding = FragmentPlanDetalleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupObservers()
        setupClickListeners()
        
        return root
    }

    private fun setupObservers() {
        planComidasViewModel.selectedPlan.observe(viewLifecycleOwner) { plan ->
            plan?.let {
                setupPlanDetails(it)
            }
        }
    }

    private fun setupPlanDetails(plan: PlanComida) {
        binding.apply {
            tvTituloDetalle.text = plan.nombre
            tvDescripcionDetalle.text = plan.descripcion
            tvCaloriasDetalle.text = "${plan.calorias} kcal"
            tvDuracionDetalle.text = plan.duracion
            tvObjetivoDetalle.text = "Plan Nutricional" // Usando un valor por defecto ya que objetivo no existe

            // Mostrar características si el tipoDieta no es nulo
            if (plan.tipoDieta != null) {
                cardCaracteristicas.visibility = View.VISIBLE
                // TODO: Configurar RecyclerView para características
            } else {
                cardCaracteristicas.visibility = View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnVerRecetas.setOnClickListener {
            // TODO: Navegar a la lista de recetas del plan
            Toast.makeText(context, "Navegando a recetas del plan...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlanDetalleFragment()
    }
}
