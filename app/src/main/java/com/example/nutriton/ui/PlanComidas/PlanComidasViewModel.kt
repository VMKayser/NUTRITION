package com.example.nutriton.ui.PlanComidas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanComidasViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Plan Comidas Fragment"
    }
    val text: LiveData<String> = _text
}