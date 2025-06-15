package com.example.nutriton.ui.Perfil

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriton.BuildConfig
import com.example.nutriton.model.NutritionRequest
import com.example.nutriton.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Perfil Fragment"
    }
    val text: LiveData<String> = _text

    private val _apiResult = MutableLiveData<String>()
    val apiResult: LiveData<String> = _apiResult

    fun analizarNutricionPrueba() {
        val request = NutritionRequest(
            ingr = listOf("1 huevo", "100g arroz")
        )
        val appId = BuildConfig.NUTRITION_APP_ID // El ID de la API
        val appKey = BuildConfig.NUTRITION_APP_KEY // Tu API KEY
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.analyzeNutrition(
                    appId = appId,
                    appKey = appKey,
                    body = request
                )
                if (response.isSuccessful) {
                    _apiResult.postValue(response.body().toString())
                } else {
                    _apiResult.postValue(response.errorBody()?.string() ?: "Error desconocido")
                }
            } catch (e: Exception) {
                _apiResult.postValue("Fallo: ${'$'}e")
            }
        }
    }

}
