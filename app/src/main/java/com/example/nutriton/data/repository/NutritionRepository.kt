package com.example.nutriton.data.repository

import android.content.Context
import com.example.nutriton.data.api.ApiClient
import com.example.nutriton.data.api.ApiResult
import com.example.nutriton.data.api.toApiError
import com.example.nutriton.data.api.models.*
import com.example.nutriton.database.NutritionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toLocalDateTime
import retrofit2.Response
import java.time.LocalDate

class NutritionRepository private constructor(private val database: NutritionDatabase) {

    private val api = ApiClient.nutritionApi
      // Función helper para manejar llamadas de API
    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<ApiResponse<T>>
    ): ApiResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true && apiResponse.data != null) {
                        ApiResult.Success(apiResponse.data)
                    } else {
                        ApiResult.Error(Exception("Error en la API: ${apiResponse?.message ?: "Respuesta vacía"}"))
                    }
                } else {
                    ApiResult.Error(
                        Exception("Error del servidor: ${response.code()} - ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                ApiResult.Error(e.toApiError())
            }
        }
    }
    
    // Función helper específica para endpoints que devuelven ApiResponse directamente
    private suspend fun <T> safeApiCallDirect(
        apiCall: suspend () -> Response<ApiResponse<T>>
    ): ApiResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true && apiResponse.data != null) {
                        ApiResult.Success(apiResponse.data)
                    } else {
                        ApiResult.Error(Exception("Error en la API: ${apiResponse?.message ?: "Respuesta vacía"}"))
                    }
                } else {
                    ApiResult.Error(
                        Exception("Error del servidor: ${response.code()} - ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                ApiResult.Error(e.toApiError())
            }
        }
    }
    
    suspend fun getRecetas(): ApiResult<List<Receta>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getRecetas()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true && apiResponse.data?.recetas != null) {
                        ApiResult.Success(apiResponse.data.recetas)
                    } else {
                        ApiResult.Error(Exception("Error en la API: ${apiResponse?.message ?: "Respuesta vacía"}"))
                    }
                } else {
                    ApiResult.Error(
                        Exception("Error del servidor: ${response.code()} - ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                ApiResult.Error(e.toApiError())
            }
        }
    }    suspend fun getRecetaById(id: Int): ApiResult<Receta> {
        return safeApiCallDirect { api.getRecetaById(id) }
    }    suspend fun getRecetasFiltradas(
        categoria: String? = null,
        tipoDieta: String? = null,
        objetivo: String? = null,
        caloriasMin: Int? = null,
        caloriasMax: Int? = null
    ): ApiResult<List<Receta>> {
        return withContext(Dispatchers.IO) {
            try {
                println("DEBUG Repository: Llamando API con tipoDieta='$tipoDieta'")
                val response = api.getRecetasFiltradas(categoria, tipoDieta, objetivo, caloriasMin, caloriasMax)
                println("DEBUG Repository: Response code: ${response.code()}")
                  if (response.isSuccessful) {
                    val apiResponse = response.body()
                    println("DEBUG Repository: ApiResponse success: ${apiResponse?.success}")
                    println("DEBUG Repository: ApiResponse data: ${apiResponse?.data}")
                    println("DEBUG Repository: Filtros aplicados: ${apiResponse?.data?.filtros_aplicados}")
                      if (apiResponse?.success == true && apiResponse.data?.recetas != null) {
                        println("DEBUG Repository: Recetas encontradas: ${apiResponse.data.recetas.size}")
                        println("DEBUG Repository: Filtros aplicados: ${apiResponse.data.filtros_aplicados}")
                        
                        // El backend ya implementa el filtrado correcto, no necesitamos filtrar aquí
                        val recetas = apiResponse.data.recetas
                        
                        println("DEBUG Repository: Recetas después del filtrado del backend: ${recetas.size}")
                        recetas.forEachIndexed { index, receta ->
                            println("DEBUG Repository: Receta $index: ${receta.id} - ${receta.nombre}")
                            println("  - Tipos de dieta: ${receta.tipos_dieta?.map { it.nombre } ?: emptyList()}")
                        }
                        
                        ApiResult.Success(recetas)
                    } else {
                        println("DEBUG Repository: Error - success=${apiResponse?.success}, data=${apiResponse?.data}")
                        ApiResult.Error(Exception("Error en la API: ${apiResponse?.message ?: "Respuesta vacía"}"))
                    }
                } else {
                    println("DEBUG Repository: Error del servidor: ${response.code()}")
                    ApiResult.Error(
                        Exception("Error del servidor: ${response.code()} - ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                println("DEBUG Repository: Exception: ${e.message}")
                e.printStackTrace()
                ApiResult.Error(e.toApiError())
            }
        }
    }
    
    suspend fun getCategorias(): ApiResult<List<CategoriaComida>> {
        return safeApiCallDirect { api.getCategorias() }
    }
    
    suspend fun getTiposDieta(): ApiResult<List<TipoDieta>> {
        return safeApiCallDirect { api.getTiposDieta() }
    }
      suspend fun getObjetivos(): ApiResult<List<Objetivo>> {
        return safeApiCallDirect { api.getObjetivos() }
    }
    
    // ============= Funciones de Progreso Diario =============
    
    suspend fun obtenerProgresoDia(fecha: LocalDate, usuarioId: Int = 1) = 
        database.progresoDiarioDao().obtenerProgresoDia(fecha, usuarioId)
    
    suspend fun obtenerProgresoRango(fechaInicio: LocalDate, fechaFin: LocalDate, usuarioId: Int = 1) = 
        database.progresoDiarioDao().obtenerProgresoRango(fechaInicio, fechaFin, usuarioId)
    
    fun observarProgresoHoy(fecha: LocalDate, usuarioId: Int = 1) = 
        database.progresoDiarioDao().observarProgresoHoy(fecha, usuarioId)
    
    suspend fun marcarComidaCompletada(fecha: LocalDate, tipoComida: String, completado: Boolean, usuarioId: Int = 1) {
        database.progresoDiarioDao().crearProgresoSiNoExiste(fecha, usuarioId)
        database.progresoDiarioDao().marcarComidaCompletada(fecha, tipoComida, completado, usuarioId)
    }
    
    suspend fun incrementarVasoAgua(fecha: LocalDate, usuarioId: Int = 1) {
        database.progresoDiarioDao().crearProgresoSiNoExiste(fecha, usuarioId)
        database.progresoDiarioDao().incrementarVasoAgua(fecha, usuarioId)
    }
    
    suspend fun decrementarVasoAgua(fecha: LocalDate, usuarioId: Int = 1) {
        database.progresoDiarioDao().crearProgresoSiNoExiste(fecha, usuarioId)
        database.progresoDiarioDao().decrementarVasoAgua(fecha, usuarioId)
    }
    
    suspend fun obtenerRachaActual(fechaActual: LocalDate, usuarioId: Int = 1) = 
        database.progresoDiarioDao().obtenerRachaActual(fechaActual, usuarioId)
    
    companion object {
        @Volatile
        private var INSTANCE: NutritionRepository? = null
        
        fun getInstance(context: Context): NutritionRepository {
            return INSTANCE ?: synchronized(this) {
                val database = NutritionDatabase.getDatabase(context)
                INSTANCE ?: NutritionRepository(database).also { INSTANCE = it }            }
        }

        // Para mantener compatibilidad con código existente (temporal)
        fun getInstance(): NutritionRepository {
            return INSTANCE ?: throw IllegalStateException(
                "Repository no inicializado. Llama a getInstance(context) primero."
            )
        }
    }

    // ============== FUNCIONES DE PREFERENCIAS ==============

    /**
     * Obtiene las preferencias almacenadas localmente
     */
    suspend fun obtenerPreferenciasLocal(): com.example.nutriton.database.entities.PreferenciasUsuario? {
        return withContext(Dispatchers.IO) {
            database.preferenciasDao().obtenerPreferencias()
        }
    }

    /**
     * Guarda las preferencias localmente en Room
     */
    suspend fun guardarPreferenciasLocal(preferencias: com.example.nutriton.database.entities.PreferenciasUsuario) {
        withContext(Dispatchers.IO) {
            database.preferenciasDao().guardarPreferencias(preferencias)
        }
    }

    /**
     * Sincroniza preferencias con el servidor
     */
    suspend fun sincronizarPreferencias(): ApiResult<String> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Obtener preferencias locales
                val preferenciaLocal = database.preferenciasDao().obtenerPreferencias()
                
                if (preferenciaLocal != null) {
                    // 2. Convertir a modelo API
                    val preferenciasApi = PreferenciasUsuarioApi(
                        usuario_id = 1,
                        tipo_dieta_id = preferenciaLocal.tipo_dieta_id,
                        objetivo_id = preferenciaLocal.objetivo_id,
                        porcentaje_proteinas = preferenciaLocal.porcentaje_proteinas,
                        porcentaje_carbohidratos = preferenciaLocal.porcentaje_carbohidratos,
                        porcentaje_grasas = preferenciaLocal.porcentaje_grasas,
                        nivel_actividad = preferenciaLocal.nivel_actividad,
                        peso_objetivo = preferenciaLocal.peso_objetivo,
                        fecha_actualizacion = preferenciaLocal.fecha_actualizacion.toString()
                    )
                    
                    // 3. Enviar al servidor
                    val response = api.guardarPreferencias(preferenciasApi)
                    if (response.isSuccessful) {
                        ApiResult.Success("Preferencias sincronizadas correctamente")
                    } else {
                        ApiResult.Error(Exception("Error al sincronizar: ${response.message()}"))
                    }
                } else {
                    ApiResult.Error(Exception("No hay preferencias locales para sincronizar"))
                }
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
    }

    /**
     * Descarga preferencias del servidor y las guarda localmente
     */
    suspend fun descargarPreferencias(): ApiResult<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPreferenciasUsuario(1)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true && apiResponse.data != null) {
                        // Convertir de API a entidad local
                        val preferenciasLocal = com.example.nutriton.database.entities.PreferenciasUsuario(
                            id = 1,
                            tipo_dieta_id = apiResponse.data.tipo_dieta_id,
                            objetivo_id = apiResponse.data.objetivo_id,
                            porcentaje_proteinas = apiResponse.data.porcentaje_proteinas,
                            porcentaje_carbohidratos = apiResponse.data.porcentaje_carbohidratos,
                            porcentaje_grasas = apiResponse.data.porcentaje_grasas,
                            nivel_actividad = apiResponse.data.nivel_actividad,
                            peso_objetivo = apiResponse.data.peso_objetivo,
                            fecha_actualizacion = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                        )
                        
                        // Guardar localmente
                        database.preferenciasDao().guardarPreferencias(preferenciasLocal)
                        ApiResult.Success("Preferencias descargadas correctamente")
                    } else {
                        ApiResult.Error(Exception("No se encontraron preferencias en el servidor"))
                    }
                } else {
                    ApiResult.Error(Exception("Error al descargar preferencias: ${response.message()}"))
                }
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
    }

    // ============== FUNCIONES DE PROGRESO DIARIO ==============

    /**
     * Obtiene el progreso diario local
     */
    suspend fun getProgresoDiario(fecha: LocalDate): com.example.nutriton.database.entities.ProgresoDiario? {
        return withContext(Dispatchers.IO) {
            database.progresoDiarioDao().obtenerProgresoDia(fecha)
        }
    }

    /**
     * Guarda el progreso diario localmente
     */
    suspend fun guardarProgresoDiario(progreso: com.example.nutriton.database.entities.ProgresoDiario) {
        withContext(Dispatchers.IO) {
            database.progresoDiarioDao().insertarOActualizarProgreso(progreso)
        }
    }

    /**
     * Sincroniza el progreso diario con el servidor
     */
    suspend fun sincronizarProgresoDiario(fecha: LocalDate): ApiResult<String> {
        return withContext(Dispatchers.IO) {
            try {
                val progresoLocal = database.progresoDiarioDao().obtenerProgresoDia(fecha)

                if (progresoLocal != null) {
                    val progresoApi = ProgresoDiarioApi(
                        usuario_id = progresoLocal.usuario_id,
                        fecha = fecha.toString(),
                        desayuno_completado = progresoLocal.desayuno_completado,
                        almuerzo_completado = progresoLocal.almuerzo_completado,
                        cena_completada = progresoLocal.cena_completada,
                        vasos_agua = progresoLocal.vasos_agua_consumidos,
                        objetivo_vasos = progresoLocal.vasos_agua_objetivo,
                        peso_registrado = 0.0f, // Este campo no existe en tu entidad
                        notas = "" // Este campo no existe en tu entidad
                    )
                    
                    val response = api.guardarProgreso(progresoApi)
                    if (response.isSuccessful) {
                        ApiResult.Success("Progreso sincronizado correctamente")
                    } else {
                        ApiResult.Error(Exception("Error al sincronizar progreso: ${response.message()}"))
                    }
                } else {
                    ApiResult.Error(Exception("No hay progreso local para sincronizar"))
                }
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
    }
}
