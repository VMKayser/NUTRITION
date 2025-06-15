package com.example.nutriton.ui.Progreso

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProgresoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Progreso Fragment"
    }
    val text: LiveData<String> = _text
}