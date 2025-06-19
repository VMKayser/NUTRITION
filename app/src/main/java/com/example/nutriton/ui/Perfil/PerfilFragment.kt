package com.example.nutriton.ui.Perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.nutriton.R
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.navigation.fragment.findNavController

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Configurar navegación
        setupNavigation(view)
        
        // Configurar spinner de género
        setupSpinnerGenero(view)
    }

    private fun setupSpinnerGenero(view: View) {
        val spinnerGenero = view.findViewById<Spinner>(R.id.spinner_genero)
        val generos = arrayOf("Seleccionar género", "Masculino", "Femenino", "Otro")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            generos
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adapter
    }    private fun setupNavigation(view: View) {
        view.findViewById<LinearLayout>(R.id.layout_gestionar_metas).setOnClickListener {
            findNavController().navigate(R.id.action_perfil_to_gestionarMetas)
        }
        
        view.findViewById<LinearLayout>(R.id.layout_preferencias_nutricion).setOnClickListener {
            findNavController().navigate(R.id.action_perfil_to_preferenciasNutricion)
        }
        
        view.findViewById<LinearLayout>(R.id.layout_nivel_actividad).setOnClickListener {
            findNavController().navigate(R.id.action_perfil_to_nivelActividad)
        }
    }
}