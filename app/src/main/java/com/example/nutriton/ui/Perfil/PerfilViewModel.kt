package com.example.nutriton.ui.Perfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nutriton.database.entities.Usuario
import com.example.nutriton.repository.NutritionRepository
import com.example.nutriton.utils.ConfiguracionesManager
import com.example.nutriton.utils.ConfiguracionesPerfil
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NutritionRepository(application)
    private val configuracionesManager = ConfiguracionesManager(application)

    // LiveData para Usuario
    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    // LiveData para estados de carga y mensajes
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    // LiveData para configuraciones (switches)
    private val _configuraciones = MutableLiveData<ConfiguracionesPerfil>()
    val configuraciones: LiveData<ConfiguracionesPerfil> = _configuraciones

    // LiveData para controlar si hay cambios sin guardar
    private val _hayCambiosPendientes = MutableLiveData<Boolean>()
    val hayCambiosPendientes: LiveData<Boolean> = _hayCambiosPendientes

    // Variables temporales para almacenar cambios antes de guardar
    private var datosTemporales: DatosTemporales? = null

    init {
        cargarDatosUsuario()
        cargarConfiguraciones()
    }

    private fun cargarDatosUsuario() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val usuarioActual = repository.obtenerUsuario()
                _usuario.value = usuarioActual
                
                // Si no existe usuario, crear uno por defecto
                if (usuarioActual == null) {
                    val nuevoUsuario = Usuario(
                        id = 1,
                        nombre = "",
                        edad = null,
                        altura = null,
                        peso_actual = null,
                        genero = null,
                        fecha_registro = Clock.System.todayIn(TimeZone.currentSystemDefault())
                    )
                    repository.guardarUsuario(nuevoUsuario)
                    _usuario.value = nuevoUsuario
                }
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar datos del usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }    private fun cargarConfiguraciones() {
        // Cargar configuraciones desde SharedPreferences
        _configuraciones.value = configuracionesManager.obtenerTodasLasConfiguraciones()
    }fun guardarDatosPersonales(
        nombre: String,
        peso: String,
        estatura: String,
        edad: String,
        genero: String
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val usuarioActual = _usuario.value ?: return@launch
                
                val usuarioActualizado = usuarioActual.copy(
                    nombre = nombre.trim(),
                    peso_actual = peso.toFloatOrNull(),
                    altura = estatura.toFloatOrNull(),
                    edad = edad.toIntOrNull(),
                    genero = if (genero.contains("Seleccionar")) null else genero
                )
                
                repository.guardarUsuario(usuarioActualizado)
                _usuario.value = usuarioActualizado
                
                // Si cambió el peso, agregarlo al historial
                if (peso.toFloatOrNull() != null && peso.toFloatOrNull() != usuarioActual.peso_actual) {
                    repository.agregarPeso(peso.toFloat(), "Actualización desde perfil")
                }
                
                // Limpiar datos temporales y marcar como guardado
                datosTemporales = null
                _hayCambiosPendientes.value = false
                _mensaje.value = "Datos guardados correctamente"
                
            } catch (e: Exception) {
                _mensaje.value = "Error al guardar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Nuevo método para actualizar datos temporalmente (sin guardar)
    fun actualizarDatosTemporales(
        nombre: String? = null,
        peso: String? = null,
        estatura: String? = null,
        edad: String? = null,
        genero: String? = null
    ) {
        // Actualizar datos temporales
        datosTemporales = DatosTemporales(
            nombre = nombre ?: datosTemporales?.nombre,
            peso = peso ?: datosTemporales?.peso,
            estatura = estatura ?: datosTemporales?.estatura,
            edad = edad ?: datosTemporales?.edad,
            genero = genero ?: datosTemporales?.genero
        )
        
        // Verificar si hay cambios comparando con el usuario actual
        val usuarioActual = _usuario.value
        val hayCambios = usuarioActual?.let { usuario ->
            (datosTemporales?.nombre != null && datosTemporales?.nombre?.trim() != usuario.nombre) ||
            (datosTemporales?.peso != null && datosTemporales?.peso?.toFloatOrNull() != usuario.peso_actual) ||
            (datosTemporales?.estatura != null && datosTemporales?.estatura?.toFloatOrNull() != usuario.altura) ||
            (datosTemporales?.edad != null && datosTemporales?.edad?.toIntOrNull() != usuario.edad) ||
            (datosTemporales?.genero != null && 
             !datosTemporales?.genero!!.contains("Seleccionar") && 
             datosTemporales?.genero != usuario.genero)
        } ?: false
        
        _hayCambiosPendientes.value = hayCambios
    }

    // Método para guardar solo si hay cambios pendientes
    fun guardarCambiosPendientes() {
        val datos = datosTemporales
        if (datos != null && _hayCambiosPendientes.value == true) {
            guardarDatosPersonales(
                nombre = datos.nombre ?: _usuario.value?.nombre ?: "",
                peso = datos.peso ?: _usuario.value?.peso_actual?.toString() ?: "",
                estatura = datos.estatura ?: _usuario.value?.altura?.toString() ?: "",
                edad = datos.edad ?: _usuario.value?.edad?.toString() ?: "",
                genero = datos.genero ?: _usuario.value?.genero ?: "Seleccionar género"
            )
        } else {
            _mensaje.value = "No hay cambios para guardar"
        }
    }    // Métodos para actualizar configuraciones individuales
    fun actualizarRecordatorioAgua(activo: Boolean) {
        configuracionesManager.setRecordatorioAgua(activo)
        cargarConfiguraciones() // Recargar para actualizar UI
    }

    fun actualizarSeguimientoDieta(activo: Boolean) {
        configuracionesManager.setSeguimientoDieta(activo)
        cargarConfiguraciones()
    }

    fun actualizarPesajeAutomatico(activo: Boolean) {
        configuracionesManager.setPesajeAutomatico(activo)
        cargarConfiguraciones()
    }

    fun actualizarModoOscuro(activo: Boolean) {
        configuracionesManager.setModoOscuro(activo)
        cargarConfiguraciones()
    }

    fun limpiarMensaje() {
        _mensaje.value = ""
    }
}

// Data class para almacenar datos temporales antes de guardar
data class DatosTemporales(
    val nombre: String? = null,
    val peso: String? = null,
    val estatura: String? = null,
    val edad: String? = null,
    val genero: String? = null
)
