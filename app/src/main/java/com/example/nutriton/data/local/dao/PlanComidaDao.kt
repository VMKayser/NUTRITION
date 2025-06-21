package com.example.nutriton.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nutriton.data.local.entities.*

@Dao
interface PlanComidaDao {
    
    @Query("SELECT * FROM planes_comida_local WHERE id = :planId")
    suspend fun getPlanById(planId: Int): PlanComidaLocal?
    
    @Query("SELECT * FROM planes_comida_local")
    fun getAllPlanes(): LiveData<List<PlanComidaLocal>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: PlanComidaLocal)
    
    @Query("SELECT * FROM dias_plan WHERE planId = :planId ORDER BY numeroDia")
    suspend fun getDiasByPlan(planId: Int): List<DiaPlan>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiaPlan(diaPlan: DiaPlan)
    
    @Query("SELECT * FROM comidas_dia WHERE diaPlanId = :diaPlanId ORDER BY categoriaComidaId, orden")
    suspend fun getComidasByDia(diaPlanId: Int): List<ComidaDia>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComidaDia(comidaDia: ComidaDia)
    
    @Query("""
        SELECT cd.*, 
               'Receta Demo' as recetaNombre,
               'Descripción de receta' as recetaDescripcion,
               NULL as recetaImagenUrl,
               500 as calorias,
               1 as porciones,
               30 as tiempoPreparacion,
               CASE cd.categoriaComidaId 
                   WHEN 1 THEN 'Desayuno'
                   WHEN 2 THEN 'Almuerzo' 
                   WHEN 3 THEN 'Cena'
                   ELSE 'Comida'
               END as categoriaNombre
        FROM comidas_dia cd 
        WHERE cd.diaPlanId = :diaPlanId 
        ORDER BY cd.categoriaComidaId, cd.orden
    """)
    suspend fun getComidasCompletasByDia(diaPlanId: Int): List<ComidaCompleta>
    
    @Query("DELETE FROM planes_comida_local WHERE id = :planId")
    suspend fun deletePlan(planId: Int)
    
    @Query("DELETE FROM dias_plan WHERE planId = :planId")
    suspend fun deleteDiasByPlan(planId: Int)
    
    @Query("DELETE FROM comidas_dia WHERE diaPlanId IN (SELECT id FROM dias_plan WHERE planId = :planId)")
    suspend fun deleteComidasByPlan(planId: Int)
}
