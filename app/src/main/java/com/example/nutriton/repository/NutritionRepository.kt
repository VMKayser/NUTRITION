package com.example.nutriton.repository

import android.content.Context
import com.example.nutriton.database.NutritionDatabase
import com.example.nutriton.database.dao.PreferenciasCompletas
import com.example.nutriton.database.entities.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Duration.Companion.days

class NutritionRepository(context: Context) {
    
    private val database = NutritionDatabase.getDatabase(context)
    
    // === USUARIO ===
    suspend fun obtenerUsuario() = database.usuarioDao().obtenerUsuario()
    suspend fun guardarUsuario(usuario: Usuario) = database.usuarioDao().guardarUsuario(usuario)
    suspend fun actualizarUsuario(usuario: Usuario) = database.usuarioDao().actualizarUsuario(usuario)
    
    // === TIPOS DE DIETA ===
    suspend fun obtenerTiposDieta() = database.tipoDietaLocalDao().obtenerTiposDieta()
    suspend fun sincronizarTiposDieta(tipos: List<TipoDietaLocal>) = 
        database.tipoDietaLocalDao().insertarTiposDieta(tipos)
    suspend fun obtenerTipoDietaPorId(id: Int) = database.tipoDietaLocalDao().obtenerTipoDietaPorId(id)
    
    // === OBJETIVOS ===
    suspend fun obtenerObjetivos() = database.objetivoLocalDao().obtenerObjetivos()
    suspend fun sincronizarObjetivos(objetivos: List<ObjetivoLocal>) = 
        database.objetivoLocalDao().insertarObjetivos(objetivos)
    suspend fun obtenerObjetivoPorId(id: Int) = database.objetivoLocalDao().obtenerObjetivoPorId(id)
    
    // === CATEGORÍAS ===
    suspend fun obtenerCategorias() = database.categoriaComidaLocalDao().obtenerCategorias()
    suspend fun sincronizarCategorias(categorias: List<CategoriaComidaLocal>) = 
        database.categoriaComidaLocalDao().insertarCategorias(categorias)
    suspend fun obtenerCategoriaPorId(id: Int) = database.categoriaComidaLocalDao().obtenerCategoriaPorId(id)
    
    // === PREFERENCIAS ===
    suspend fun obtenerPreferencias() = database.preferenciasDao().obtenerPreferencias()
    suspend fun obtenerPreferenciasCompletas() = database.preferenciasDao().obtenerPreferenciasCompletas()
    suspend fun guardarPreferencias(preferencias: PreferenciasUsuario) = 
        database.preferenciasDao().guardarPreferencias(preferencias)
    suspend fun actualizarPreferencias(preferencias: PreferenciasUsuario) = 
        database.preferenciasDao().actualizarPreferencias(preferencias)
    
    // === ALERGIAS ===
    suspend fun obtenerAlergias(usuarioId: Int = 1) = database.usuarioAlergiaDao().obtenerAlergias(usuarioId)
    suspend fun agregarAlergia(alergia: String, usuarioId: Int = 1) {
        val nuevaAlergia = UsuarioAlergia(
            usuario_id = usuarioId,
            alergia = alergia,
            fecha_agregada = Clock.System.todayIn(TimeZone.currentSystemDefault())
        )
        database.usuarioAlergiaDao().agregarAlergia(nuevaAlergia)
    }
    suspend fun eliminarAlergia(id: Int) = database.usuarioAlergiaDao().eliminarAlergia(id)
    suspend fun tieneAlergia(alergia: String, usuarioId: Int = 1) = 
        database.usuarioAlergiaDao().tieneAlergia(usuarioId, alergia)
    
    // === ALIMENTOS FAVORITOS ===
    suspend fun obtenerAlimentosFavoritos(usuarioId: Int = 1) = 
        database.usuarioAlimentoFavoritoDao().obtenerAlimentosFavoritos(usuarioId)
    suspend fun agregarAlimentoFavorito(alimento: String, usuarioId: Int = 1) {
        val nuevoAlimento = UsuarioAlimentoFavorito(
            usuario_id = usuarioId,
            alimento = alimento,
            fecha_agregada = Clock.System.todayIn(TimeZone.currentSystemDefault())
        )
        database.usuarioAlimentoFavoritoDao().agregarAlimentoFavorito(nuevoAlimento)
    }
    suspend fun eliminarAlimentoFavorito(id: Int) = 
        database.usuarioAlimentoFavoritoDao().eliminarAlimentoFavorito(id)
    
    // === HISTORIAL DE PESO ===
    suspend fun obtenerHistorialPeso(usuarioId: Int = 1) = 
        database.historialPesoDao().obtenerHistorial(usuarioId)
    suspend fun agregarPeso(peso: Float, notas: String = "", usuarioId: Int = 1) {
        val nuevoPeso = HistorialPeso(
            usuario_id = usuarioId,
            peso = peso,
            fecha = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            notas = notas
        )
        database.historialPesoDao().agregarPeso(nuevoPeso)
    }
    suspend fun obtenerUltimoPeso(usuarioId: Int = 1) = 
        database.historialPesoDao().obtenerUltimoPeso(usuarioId)
    
    // === CACHE DE RECETAS ===
    suspend fun obtenerRecetasCache() = database.cacheRecetasDao().obtenerRecetasCache()
    suspend fun obtenerRecetasPorCategoria(categoriaId: Int) = 
        database.cacheRecetasDao().obtenerRecetasPorCategoria(categoriaId)
    suspend fun obtenerRecetaPorId(recetaId: Int) = 
        database.cacheRecetasDao().obtenerRecetaPorId(recetaId)
    suspend fun guardarRecetaEnCache(receta: CacheRecetas) = 
        database.cacheRecetasDao().guardarRecetaEnCache(receta)
    suspend fun actualizarUltimoAcceso(recetaId: Int) {
        val ahora = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        database.cacheRecetasDao().actualizarUltimoAcceso(recetaId, ahora)
    }
    suspend fun limpiarCacheAntiguo(diasAntiguedad: Int = 30) {
        val fechaLimite = Clock.System.now().minus(diasAntiguedad.days)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        database.cacheRecetasDao().limpiarCacheAntiguo(fechaLimite)
    }
    
    // === PLAN SEMANAL ===
    suspend fun obtenerPlanDelDia(fecha: LocalDate, usuarioId: Int = 1) = 
        database.planSemanalDao().obtenerPlanDelDia(fecha, usuarioId)
    suspend fun obtenerPlanDelDiaCompleto(fecha: LocalDate, usuarioId: Int = 1) = 
        database.planSemanalDao().obtenerPlanDelDiaCompleto(fecha, usuarioId)
    suspend fun agregarAlPlan(plan: PlanSemanal) = database.planSemanalDao().agregarAlPlan(plan)
    suspend fun marcarCompletada(id: Int, completada: Boolean) = 
        database.planSemanalDao().marcarCompletada(id, completada)
    suspend fun eliminarDelPlan(id: Int) = database.planSemanalDao().eliminarDelPlan(id)
    
    // === FUNCIONES DE INICIALIZACIÓN ===
    suspend fun inicializarDatosMaestros() {
        // Datos de ejemplo para categorías
        val categorias = listOf(
            CategoriaComidaLocal(1, "Desayuno", "Comidas para empezar el día", 1, true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
            CategoriaComidaLocal(2, "Almuerzo", "Comidas del mediodía", 2, true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
            CategoriaComidaLocal(3, "Cena", "Comidas de la noche", 3, true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
            CategoriaComidaLocal(4, "Snacks", "Aperitivos y meriendas", 4, true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
        )
        sincronizarCategorias(categorias)
        
        // Datos de ejemplo para tipos de dieta
        val tiposDieta = listOf(
            TipoDietaLocal(1, "Equilibrada", "Dieta balanceada en todos los nutrientes", true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
            TipoDietaLocal(2, "Mediterránea", "Rica en aceite de oliva, pescado y vegetales", true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
            TipoDietaLocal(3, "Vegana", "Sin productos de origen animal", true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
            TipoDietaLocal(4, "Vegetariana", "Sin carne, permite lácteos y huevos", true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
            TipoDietaLocal(5, "Baja en carbohidratos", "Reducida en carbohidratos", true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
        )
        sincronizarTiposDieta(tiposDieta)
        
        // Datos de ejemplo para objetivos
        val objetivos = listOf(
            ObjetivoLocal(1, "Bajar peso", "Reducir peso corporal de forma saludable", true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
            ObjetivoLocal(2, "Ganar masa muscular", "Aumentar masa muscular", true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
            ObjetivoLocal(3, "Mantener peso", "Mantener el peso actual", true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
            ObjetivoLocal(4, "Definir músculos", "Reducir grasa y definir músculos", true, 
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
        )
        sincronizarObjetivos(objetivos)
    }
}
