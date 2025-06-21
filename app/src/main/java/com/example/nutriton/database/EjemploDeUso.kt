package com.example.nutriton.database

/**
 * EJEMPLO DE USO DE LA BASE DE DATOS LOCAL
 * 
 * Este archivo muestra cómo usar la base de datos en tus ViewModels y Fragments
 */

/*
=== EN TU VIEWMODEL ===

class PreferenciasNutricionViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = NutritionRepository(application)
    
    private val _tiposDieta = MutableLiveData<List<TipoDietaLocal>>()
    val tiposDieta: LiveData<List<TipoDietaLocal>> = _tiposDieta
    
    private val _preferencias = MutableLiveData<PreferenciasCompletas?>()
    val preferencias: LiveData<PreferenciasCompletas?> = _preferencias
    
    init {
        cargarDatos()
    }
    
    private fun cargarDatos() {
        viewModelScope.launch {
            // Cargar tipos de dieta
            _tiposDieta.value = repository.obtenerTiposDieta()
            
            // Si no hay datos, inicializar con datos por defecto
            if (_tiposDieta.value?.isEmpty() == true) {
                repository.inicializarDatosMaestros()
                _tiposDieta.value = repository.obtenerTiposDieta()
            }
            
            // Cargar preferencias actuales
            _preferencias.value = repository.obtenerPreferenciasCompletas()
        }
    }
    
    fun guardarPreferencias(
        tipoDietaId: Int,
        objetivoId: Int,
        proteinas: Int,
        carbohidratos: Int,
        grasas: Int
    ) {
        viewModelScope.launch {
            val preferencias = PreferenciasUsuario(
                tipo_dieta_id = tipoDietaId,
                objetivo_id = objetivoId,
                porcentaje_proteinas = proteinas,
                porcentaje_carbohidratos = carbohidratos,
                porcentaje_grasas = grasas,
                fecha_actualizacion = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
            
            repository.guardarPreferencias(preferencias)
            _preferencias.value = repository.obtenerPreferenciasCompletas()
        }
    }
}

=== EN TU FRAGMENT ===

class PreferenciasNutricionFragment : Fragment() {
    
    private lateinit var viewModel: PreferenciasNutricionViewModel
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPreferenciasNutricionBinding.inflate(inflater, container, false)
        
        viewModel = ViewModelProvider(this)[PreferenciasNutricionViewModel::class.java]
        
        setupObservers()
        
        return binding.root
    }
    
    private fun setupObservers() {
        // Observar tipos de dieta para llenar spinner
        viewModel.tiposDieta.observe(viewLifecycleOwner) { tipos ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                tipos.map { it.nombre }
            )
            binding.spinnerPlanDieta.adapter = adapter
        }
        
        // Observar preferencias actuales
        viewModel.preferencias.observe(viewLifecycleOwner) { preferencias ->
            preferencias?.let {
                binding.etProteinas.setText(it.porcentaje_proteinas.toString())
                binding.etCarbohidratos.setText(it.porcentaje_carbohidratos.toString())
                binding.etGrasas.setText(it.porcentaje_grasas.toString())
            }
        }
    }
}

=== EN TU MAINACTIVITY (Inicialización) ===

class MainActivity : AppCompatActivity() {
    
    private lateinit var repository: NutritionRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar repositorio
        repository = NutritionRepository(this)
        
        // Inicializar datos maestros en el primer uso
        lifecycleScope.launch {
            repository.inicializarDatosMaestros()
        }
    }
}

=== EJEMPLO DE CONSULTAS TÍPICAS ===

// Guardar usuario
val usuario = Usuario(
    nombre = "Juan Pérez",
    edad = 25,
    altura = 175f,
    peso_actual = 70f,
    genero = "M",
    fecha_registro = Clock.System.todayIn(TimeZone.currentSystemDefault())
)
repository.guardarUsuario(usuario)

// Agregar peso al historial
repository.agregarPeso(69.5f, "Bajé medio kilo esta semana")

// Agregar alergia
repository.agregarAlergia("Gluten")

// Verificar si tiene alergia
val tieneAlergia = repository.tieneAlergia("Gluten")

// Obtener plan del día
val planHoy = repository.obtenerPlanDelDiaCompleto(LocalDate.now())

*/
