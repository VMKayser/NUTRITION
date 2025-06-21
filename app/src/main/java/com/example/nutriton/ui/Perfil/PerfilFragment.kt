package com.example.nutriton.ui.Perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nutriton.R
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.navigation.fragment.findNavController
import androidx.core.widget.addTextChangedListener

class PerfilFragment : Fragment() {

    private lateinit var viewModel: PerfilViewModel
      // Referencias a las vistas
    private lateinit var etNombreUsuario: EditText
    private lateinit var etPeso: EditText
    private lateinit var etEstatura: EditText
    private lateinit var etEdad: EditText
    private lateinit var spinnerGenero: Spinner
    private lateinit var switchAgua: SwitchMaterial
    private lateinit var switchDieta: SwitchMaterial
    private lateinit var switchPesaje: SwitchMaterial
    private lateinit var switchModoOscuro: SwitchMaterial
    private lateinit var btnGuardarCambios: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Inicializar ViewModel
        viewModel = ViewModelProvider(this)[PerfilViewModel::class.java]
        
        // Inicializar vistas
        initViews(view)
        
        // Configurar observadores
        setupObservers()
        
        // Configurar listeners
        setupListeners()
        
        // Configurar navegación
        setupNavigation(view)
        
        // Configurar spinner de género
        setupSpinnerGenero()
    }    private fun initViews(view: View) {
        etNombreUsuario = view.findViewById(R.id.et_nombre_usuario)
        etPeso = view.findViewById(R.id.et_peso)
        etEstatura = view.findViewById(R.id.et_estatura)
        etEdad = view.findViewById(R.id.et_edad)
        spinnerGenero = view.findViewById(R.id.spinner_genero)
        switchAgua = view.findViewById(R.id.switch_agua)
        switchDieta = view.findViewById(R.id.switch_dieta)
        switchPesaje = view.findViewById(R.id.switch_pesaje)
        switchModoOscuro = view.findViewById(R.id.switch_modo_oscuro)
        btnGuardarCambios = view.findViewById(R.id.btn_guardar_cambios)
    }

    private fun setupObservers() {
        // Observar datos del usuario
        viewModel.usuario.observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                // Llenar campos con datos del usuario
                etNombreUsuario.setText(it.nombre)
                etPeso.setText(it.peso_actual?.toString() ?: "")
                etEstatura.setText(it.altura?.toString() ?: "")
                etEdad.setText(it.edad?.toString() ?: "")
                
                // Seleccionar género en spinner
                it.genero?.let { genero ->
                    val adapter = spinnerGenero.adapter as ArrayAdapter<String>
                    val position = adapter.getPosition(genero)
                    if (position >= 0) {
                        spinnerGenero.setSelection(position)
                    }
                }
            }
        }

        // Observar configuraciones
        viewModel.configuraciones.observe(viewLifecycleOwner) { config ->
            switchAgua.isChecked = config.recordatorioAgua
            switchDieta.isChecked = config.seguimientoDieta
            switchPesaje.isChecked = config.pesajeAutomatico
            switchModoOscuro.isChecked = config.modoOscuro
        }

        // Observar mensajes
        viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje.isNotEmpty()) {
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                viewModel.limpiarMensaje()
            }
        }        // Observar estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Deshabilitar botón durante carga
            btnGuardarCambios.isEnabled = !isLoading
            btnGuardarCambios.text = if (isLoading) "Guardando..." else "Guardar Cambios"
        }

        // Observar si hay cambios pendientes
        viewModel.hayCambiosPendientes.observe(viewLifecycleOwner) { hayCambios ->
            btnGuardarCambios.isEnabled = hayCambios && !(viewModel.isLoading.value ?: false)
            btnGuardarCambios.alpha = if (hayCambios) 1.0f else 0.5f
        }
    }    private fun setupListeners() {
        // Listeners para detectar cambios en los campos (sin guardar automáticamente)
        etNombreUsuario.addTextChangedListener { 
            viewModel.actualizarDatosTemporales(nombre = it.toString())
        }
        etPeso.addTextChangedListener { 
            viewModel.actualizarDatosTemporales(peso = it.toString())
        }
        etEstatura.addTextChangedListener { 
            viewModel.actualizarDatosTemporales(estatura = it.toString())
        }
        etEdad.addTextChangedListener { 
            viewModel.actualizarDatosTemporales(edad = it.toString())
        }
        
        // Listener para el spinner de género
        spinnerGenero.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val genero = parent?.getItemAtPosition(position).toString()
                viewModel.actualizarDatosTemporales(genero = genero)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        
        // Listener para el botón guardar
        btnGuardarCambios.setOnClickListener {
            viewModel.guardarCambiosPendientes()
        }
          // Listeners para switches (estos sí se guardan inmediatamente)
        switchAgua.setOnCheckedChangeListener { _, isChecked ->
            viewModel.actualizarRecordatorioAgua(isChecked)
        }
        
        switchDieta.setOnCheckedChangeListener { _, isChecked ->
            viewModel.actualizarSeguimientoDieta(isChecked)
        }
        
        switchPesaje.setOnCheckedChangeListener { _, isChecked ->
            viewModel.actualizarPesajeAutomatico(isChecked)
        }
        
        switchModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            viewModel.actualizarModoOscuro(isChecked)
        }
    }    private fun setupSpinnerGenero() {
        val generos = arrayOf("Seleccionar género", "Masculino", "Femenino", "Otro")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            generos
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adapter
    }

    private fun setupNavigation(view: View) {
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