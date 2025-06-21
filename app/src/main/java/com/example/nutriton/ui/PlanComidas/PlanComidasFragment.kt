package com.example.nutriton.ui.PlanComidas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutriton.databinding.FragmentPlancomidasBinding
import com.example.nutriton.ui.PlanComidas.adapter.PlanesComidasAdapter
import com.example.nutriton.ui.PlanComidas.model.PlanComida

class PlanComidasFragment : Fragment() {

    private var _binding: FragmentPlancomidasBinding? = null
    private val binding get() = _binding!!
    private lateinit var planComidasViewModel: PlanComidasViewModel
    private lateinit var planesAdapter: PlanesComidasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        planComidasViewModel = ViewModelProvider(this)[PlanComidasViewModel::class.java]
        _binding = FragmentPlancomidasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        return root
    }

    private fun setupRecyclerView() {
        planesAdapter = PlanesComidasAdapter { plan ->
            onPlanSelected(plan)
        }
        
        binding.rvPlanesComida.apply {
            adapter = planesAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        planComidasViewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            
            state.data?.let { planes ->
                planesAdapter.submitList(planes)
                // Ocultar mensaje de "no hay planes" si hay datos
                binding.tvNoPlanes.visibility = if (planes.isEmpty()) View.VISIBLE else View.GONE
            }
            
            state.error?.let { error ->
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                binding.tvNoPlanes.visibility = View.VISIBLE
                binding.tvNoPlanes.text = "Error al cargar planes. Toca para reintentar."
            }
        }

        planComidasViewModel.planesComida.observe(viewLifecycleOwner) { planes ->
            planesAdapter.submitList(planes)
            showContent(planes.isNotEmpty())
        }

        planComidasViewModel.selectedPlan.observe(viewLifecycleOwner) { plan ->
            plan?.let {
                // TODO: Navegar al detalle o mostrar las recetas del plan
                Toast.makeText(context, "Plan seleccionado: ${it.tipoDieta?.nombre ?: it.nombre}", Toast.LENGTH_SHORT).show()
                // Aquí podrías navegar a un fragment de recetas filtradas por tipo de dieta
                // findNavController().navigate(...)
            }
        }

        planComidasViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvPlanesComida.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        planComidasViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                showEmptyState()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener {
            planComidasViewModel.refreshPlanes()
        }
    }    private fun onPlanSelected(plan: PlanComida) {
        planComidasViewModel.selectPlan(plan)
        
        // Navegar al detalle del plan de comidas (diseño original con días y comidas)
        try {
            val bundle = Bundle().apply {
                putString("planId", plan.id)
                putString("planNombre", plan.nombre)
                putString("planDescripcion", plan.descripcion)
                putString("planImagenUrl", plan.imagenUrl)
                putString("tipoDietaNombre", plan.tipoDieta?.nombre)
            }
            findNavController().navigate(
                com.example.nutriton.R.id.action_plancomidas_to_detalleplan,
                bundle
            )
        } catch (e: Exception) {
            Toast.makeText(context, "Error al navegar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showContent(hasData: Boolean) {
        if (hasData) {
            binding.rvPlanesComida.visibility = View.VISIBLE
            binding.layoutEmptyState.visibility = View.GONE
        } else {
            showEmptyState()
        }
    }

    private fun showEmptyState() {
        binding.rvPlanesComida.visibility = View.GONE
        binding.layoutEmptyState.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}