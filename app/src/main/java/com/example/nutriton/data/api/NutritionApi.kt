package com.example.nutriton.data.api

import com.example.nutriton.data.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface NutritionApi {
    
    @GET("recetas")
    suspend fun getRecetas(): Response<ApiResponse<RecetasResponse>>
    
    @GET("recetas/{id}")
    suspend fun getRecetaById(@Path("id") id: Int): Response<ApiResponse<Receta>>
      @GET("recetas-filtradas")
    suspend fun getRecetasFiltradas(
        @Query("categoria") categoria: String? = null,
        @Query("tipo_dieta") tipoDieta: String? = null,
        @Query("objetivo") objetivo: String? = null,
        @Query("calorias_min") caloriasMin: Int? = null,
        @Query("calorias_max") caloriasMax: Int? = null
    ): Response<ApiResponse<RecetasFiltradas>>
    
    @GET("categorias")
    suspend fun getCategorias(): Response<ApiResponse<List<CategoriaComida>>>
    
    @GET("tipos-dieta")
    suspend fun getTiposDieta(): Response<ApiResponse<List<TipoDieta>>>    
    @GET("objetivos")
    suspend fun getObjetivos(): Response<ApiResponse<List<Objetivo>>>
    
    // Endpoints para preferencias del usuario
    @GET("preferencias")
    suspend fun getPreferenciasUsuario(
        @Query("usuario_id") usuarioId: Int = 1
    ): Response<ApiResponse<PreferenciasUsuarioApi>>
    
    @POST("preferencias")
    suspend fun guardarPreferencias(
        @Body preferencias: PreferenciasUsuarioApi
    ): Response<ApiResponse<String>>
    
    @PUT("preferencias")
    suspend fun actualizarPreferencias(
        @Body preferencias: PreferenciasUsuarioApi
    ): Response<ApiResponse<String>>
    
    // Endpoints para progreso diario
    @GET("progreso")
    suspend fun getProgresoDiario(
        @Query("usuario_id") usuarioId: Int = 1,
        @Query("fecha") fecha: String
    ): Response<ApiResponse<ProgresoDiarioApi>>
    
    @POST("progreso")
    suspend fun guardarProgreso(
        @Body progreso: ProgresoDiarioApi
    ): Response<ApiResponse<String>>
}
