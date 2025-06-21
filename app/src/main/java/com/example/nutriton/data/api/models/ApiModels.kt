package com.example.nutriton.data.api.models

import com.google.gson.annotations.SerializedName
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.JsonAdapter
import java.lang.reflect.Type

// Respuesta específica para el endpoint de recetas
data class RecetasResponse(
    val recetas: List<Receta>,
    val pagination: Paginacion?
)

data class Paginacion(
    val page: Int,
    val limit: Int,
    val total: Int,
    val total_pages: Int
)

// Respuesta base de la API
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val timestamp: String?
)

// Modelo para recetas
data class Receta(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val ingredientes: String,
    val instrucciones: String,
    val tiempo_preparacion: Int?,
    val nivel_dificultad: String?,
    val imagen_url: String?,
    val categoria_id: Int,
    val fecha_creacion: String?,
    val activo: Int,
    val categoria_nombre: String?,
    val informacion_nutricional: InformacionNutricional?,
    val tipos_dieta: List<TipoDieta>?,
    val objetivos: List<Objetivo>?
)

// Modelo para información nutricional
data class InformacionNutricional(
    val receta_id: Int,
    val calorias: Int,
    val proteinas: String?,
    val carbohidratos: String?,
    val grasas: String?,
    val fibra: String?,
    val azucares: String?,
    val vitaminas_minerales: String?
)

// Modelo para tipos de dieta
data class TipoDieta(
    val id: String, // Cambiado a String porque la API devuelve strings
    val nombre: String,
    val descripcion: String?,
    val imagen_url: String?, // Nuevo campo para las imágenes
    val total_recetas: Int?
)

// Modelo para categorías de comida
data class CategoriaComida(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val activo: Int?
)

// Modelo para objetivos
data class Objetivo(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val total_recetas: Int?,
    val hitos: List<Hito>?
)

// Modelo para hitos de objetivos
data class Hito(
    val id: Int,
    val objetivo_id: Int,
    val nombre: String,
    val descripcion: String?,
    val valor_objetivo: String?,
    val unidad: String?
)

// Modelo para respuesta paginada
data class PaginatedResponse<T>(
    val data: List<T>,
    val pagination: Pagination
)

data class Pagination(
    val current_page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int
)

// Deserializador personalizado para manejar filtros_aplicados que puede ser objeto o array
class FiltrosAplicadosDeserializer : JsonDeserializer<FiltrosAplicados?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): FiltrosAplicados? {
        return when {
            json == null || json.isJsonNull -> null
            json.isJsonArray -> null // Si es un array, devolvemos null
            json.isJsonObject -> try {
                context?.deserialize(json, FiltrosAplicados::class.java)
            } catch (e: Exception) {
                null // En caso de error, devolvemos null
            }
            else -> null
        }
    }
}

// Modelo para respuesta de recetas filtradas
data class RecetasFiltradas(
    val recetas: List<Receta>,
    @SerializedName("filtros_aplicados")
    @JsonAdapter(FiltrosAplicadosDeserializer::class)
    val filtros_aplicados: FiltrosAplicados?, // Nullable para manejar arrays vacíos del backend
    val pagination: Pagination
)

data class FiltrosAplicados(
    val categoria_id: String?,
    val nivel_dificultad: String?,
    val tipo_dieta_id: String?,
    val objetivo_id: String?,
    val max_tiempo: String?,
    val max_calorias: String?,
    val min_calorias: String?,
    val buscar: String?
)

// Modelos para preferencias del usuario (API)
data class PreferenciasUsuarioApi(
    val usuario_id: Int = 1,
    val tipo_dieta_id: Int?,
    val objetivo_id: Int?,
    val porcentaje_proteinas: Int = 25,
    val porcentaje_carbohidratos: Int = 50,
    val porcentaje_grasas: Int = 25,
    val nivel_actividad: String = "Moderado",
    val peso_objetivo: Float?,
    val fecha_actualizacion: String? = null
)

// Modelo para progreso diario (API)
data class ProgresoDiarioApi(
    val usuario_id: Int = 1,
    val fecha: String,
    val desayuno_completado: Boolean = false,
    val almuerzo_completado: Boolean = false,
    val cena_completada: Boolean = false,
    val vasos_agua: Int = 0,
    val objetivo_vasos: Int = 6,
    val peso_registrado: Float? = null,
    val notas: String? = null
)
