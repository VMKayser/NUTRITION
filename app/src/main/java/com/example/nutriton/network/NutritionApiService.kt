package com.example.nutriton.network

import com.example.nutriton.model.NutritionRequest
import com.example.nutriton.model.NutritionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface NutritionApiService {
    @POST("api/nutrition-details")
    suspend fun analyzeNutrition(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Body body: NutritionRequest
    ): Response<NutritionResponse>
}
